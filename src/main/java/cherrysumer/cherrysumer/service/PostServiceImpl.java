package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.Participate;
import cherrysumer.cherrysumer.domain.Post;
import cherrysumer.cherrysumer.domain.PostLikes;
import cherrysumer.cherrysumer.domain.User;
import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.exception.handler.PostErrorHandler;
import cherrysumer.cherrysumer.repository.ParticipateRepository;
import cherrysumer.cherrysumer.repository.PostLikesRepository;
import cherrysumer.cherrysumer.repository.PostRepository;
import cherrysumer.cherrysumer.util.TimeUtil;
import cherrysumer.cherrysumer.web.dto.PostRequestDTO;
import cherrysumer.cherrysumer.web.dto.PostResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final PostLikesRepository likesRepository;
    private final UserService userService;
    private final ParticipateRepository participateRepository;

    // 게시글 저장
    @Override
    public PostResponseDTO.successPostDTO savePost(PostRequestDTO.addPostDTO request) {
        Post post = save(request);
        return new PostResponseDTO.successPostDTO(post);
    }

    private Post save(PostRequestDTO.addPostDTO request) {
        User user = userService.getLoggedInUser();

        Post post = new Post();
        post.setUser(user);
        post.setTitle(request.getTitle());
        post.setCapacity(request.getCapacity());
        post.setDate(request.getDate());
        post.setCategory(request.getCategory());
        post.setPrice(request.getPrice());
        post.setPlace(request.getPlace());
        post.setDetailed_category(request.getDetailed_category());
        post.setContent(request.getContent());
        post.setRegion(user.getRegion());

        return postRepository.save(post);
    }

    // 게시글 조회
    @Override
    public List<PostResponseDTO.postDTO> findPlacePosts() {
        return getPlacePosts();
    }

    private List<PostResponseDTO.postDTO> getPlacePosts() {
        User user = userService.getLoggedInUser();
        List<Post> posts = postRepository.findByRegion(user.getRegion());
        if(posts.isEmpty())
            throw new PostErrorHandler(ErrorCode._POST_BAD_REQUEST);

        List<PostResponseDTO.postDTO> list = posts.stream()
                .map((Post p) -> convertPost(p, user))
                .collect(Collectors.toList());

        return list;
    }

    // 게시글 삭제
    @Override
    public void deletePost(Long postId) {
        delete(postId);
    }

    private void delete(Long postId) {
        User user = userService.getLoggedInUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostErrorHandler(ErrorCode._POST_NOT_FOUND));

        if(post.getUser().equals(user)) {
            postRepository.delete(post);
        } else
            throw new PostErrorHandler(ErrorCode._POST_FORBIDDEN);
    }

    // 공구 참여 신청
    @Override
    public PostResponseDTO.successPostDTO participatePost(Long postId) {
        Post post = participate(postId);
        return new PostResponseDTO.successPostDTO(post);
    }

    private Post participate(Long postId) {
        User user = userService.getLoggedInUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostErrorHandler(ErrorCode._POST_NOT_FOUND));

        if(post.getUser().equals(user)) {
            throw new PostErrorHandler(ErrorCode._POST_NOT_PARTICIPATE);
        }

        Participate p = new Participate();
        p.setUser(user);
        p.setPost(post);
        p.setStatus(2);
        participateRepository.save(p);

        return post;
    }

    // 게시글 관심 목록 추가, 삭제
    @Override
    public PostResponseDTO.likePostDTO likePost(Long postId) {
        PostLikes like = likes(postId);
        return new PostResponseDTO.likePostDTO(postId, like.isStatus());
    }

    private PostLikes likes(Long postId) {
        User user = userService.getLoggedInUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostErrorHandler(ErrorCode._POST_NOT_FOUND));

        PostLikes like;
        if(likesRepository.existsByPostAndUser(post, user)) {
            like = likesRepository.findByPostAndUser(post,user);
            like.setStatus(!like.isStatus());
            likesRepository.save(like);
        } else {
            like = new PostLikes();
            like.setPost(post);
            like.setUser(user);
            like.setStatus(true);
        }

        likesRepository.save(like);
        return like;
    }

    // post 응답 객체 변환
    private PostResponseDTO.postDTO convertPost(Post p, User user) {
        int likes = Integer.parseInt(String.valueOf(likesRepository.countByPost(p)));
        boolean status;

        if(likesRepository.existsByPostAndUser(p, user)) {
            status = likesRepository.findByPostAndUser(p, user).isStatus();
        } else {
            status = false;
        }

        String upload = TimeUtil.convertTime(p.getUpdatedAt());
        PostResponseDTO.postDTO post = new PostResponseDTO.postDTO(p, likes, status, upload);

        return post;
    }
}
