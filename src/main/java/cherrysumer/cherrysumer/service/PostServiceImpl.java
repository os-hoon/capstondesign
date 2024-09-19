package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.Participate;
import cherrysumer.cherrysumer.domain.Post;
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

        return postRepository.save(post);
    }

    // 게시글 조회
    @Override
    public List<PostResponseDTO.postDTO> findPlacePosts() {
        return getPlacePosts();
    }

    private List<PostResponseDTO.postDTO> getPlacePosts() {
        User user = userService.getLoggedInUser();
        List<Post> posts = postRepository.findByPlace(user.getRegion());
        if(posts.isEmpty())
            throw new PostErrorHandler(ErrorCode._POST_BAD_REQUEST);

        List<PostResponseDTO.postDTO> list = posts.stream()
                .map((Post p) -> convertPost(p, user))
                .collect(Collectors.toList());

        return list;
    }

    // 모집 게시글 조회
    @Override
    public List<PostResponseDTO.recruitDTO> findRecruitList() {
        return getRecruitPosts();
    }

    private List<PostResponseDTO.recruitDTO> getRecruitPosts() {
        User user = userService.getLoggedInUser();
        List<Post> posts = postRepository.findByUser(user);
        if(posts.isEmpty())
            throw new PostErrorHandler(ErrorCode._POST_BAD_REQUEST);

        List<PostResponseDTO.recruitDTO> list = posts.stream()
                .map((Post p) -> convertRecruitPost(p))
                .collect(Collectors.toList());

        return list;

    }

    // 참여 신청 게시글 조회
    @Override
    public List<PostResponseDTO.applicationDTO> findApplicationList() {
        return getApplicationPosts();
    }

    private List<PostResponseDTO.applicationDTO> getApplicationPosts() {
        User user = userService.getLoggedInUser();
        List<Participate> participates = participateRepository.findAllByUser(user);
        if(participates.isEmpty())
            throw new PostErrorHandler(ErrorCode._POST_BAD_REQUEST);

        List<PostResponseDTO.applicationDTO> list = participates.stream()
                .map((Participate p) -> convertApplicationPost(p))
                .collect(Collectors.toList());

        return list;
    }

    // post 응답 객체 변환
    private PostResponseDTO.postDTO convertPost(Post p, User user) {
        int likes = Integer.parseInt(String.valueOf(likesRepository.countByPost(p)));
        boolean status = likesRepository.existsByPostAndUser(p, user);
        String upload = TimeUtil.convertTime(p.getUpdatedAt());
        PostResponseDTO.postDTO post = new PostResponseDTO.postDTO(p, likes, status, upload);

        return post;
    }

    private PostResponseDTO.recruitDTO convertRecruitPost(Post p) {
        int number = Integer.parseInt(String.valueOf(participateRepository.countAllByPost(p)));

        PostResponseDTO.recruitDTO post = new PostResponseDTO.recruitDTO(p.getTitle(), number, p.getId());
        return post;
    }

    private PostResponseDTO.applicationDTO convertApplicationPost(Participate p) {
        PostResponseDTO.applicationDTO post =
                new PostResponseDTO.applicationDTO(p.getPost().getTitle(), p.getPost().getId(), p.getStatus());
        return post;
    }
}
