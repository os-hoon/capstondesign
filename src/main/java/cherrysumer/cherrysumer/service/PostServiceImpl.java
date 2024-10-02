package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.Participate;
import cherrysumer.cherrysumer.domain.Post;
import cherrysumer.cherrysumer.domain.PostLikes;
import cherrysumer.cherrysumer.domain.User;
import cherrysumer.cherrysumer.exception.BaseException;
import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.exception.handler.PostErrorHandler;
import cherrysumer.cherrysumer.exception.handler.UserErrorHandler;
import cherrysumer.cherrysumer.repository.ParticipateRepository;
import cherrysumer.cherrysumer.repository.PostLikesRepository;
import cherrysumer.cherrysumer.repository.PostRepository;
import cherrysumer.cherrysumer.util.TimeUtil;
import cherrysumer.cherrysumer.web.dto.PostRequestDTO;
import cherrysumer.cherrysumer.web.dto.PostResponseDTO;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final PostLikesRepository likesRepository;
    private final UserService userService;
    private final ParticipateRepository participateRepository;
    private final ParticipateService participateService;

    // 게시글 저장
    @Override
    public PostResponseDTO.summaryPostDTO savePost(PostRequestDTO.postDTO request) throws ParseException {
        Post post = save(request);
        return new PostResponseDTO.summaryPostDTO(post);
    }

    private Post save(PostRequestDTO.postDTO request) throws ParseException {
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
        post.setRegionCode(user.getRegionCode());
        post.setClosed(false);
        post.setPoint(userService.convertPoint(request.getLongitude(), request.getLatitude()));

        return postRepository.save(post);
    }

    // 게시글 조회
    @Override
    public List<PostResponseDTO.postDTO> findRegionPosts(int sotrted) {
        return getRegionPosts(sotrted);
    }

    private List<PostResponseDTO.postDTO> getRegionPosts(int sotrted) {
        // 0 : 최신순, 1 : 인기순
        if(sotrted != 0 && sotrted != 1) {
            throw new BaseException(ErrorCode._BAD_REQUEST);
        }

        User user = userService.getLoggedInUser();
        List<Post> posts = postRepository.findAllByRegionCodeOrderByCreatedAt(user.getRegionCode());

        // 인기순 정렬
        if(sotrted == 1) {
            posts.sort((o1, o2) -> (int) (likesRepository.countByPost(o2) - likesRepository.countByPost(o1)));
        }

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

    // 게시글 수정
    @Override
    public PostResponseDTO.detailPostDTO updatePost(PostRequestDTO.postDTO request) throws ParseException {
        User user = userService.getLoggedInUser();
        Post post = update(request, user);
        return convertDetailPost(post, user);
    }

    private Post update(PostRequestDTO.postDTO request, User user) throws ParseException {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new PostErrorHandler(ErrorCode._POST_NOT_FOUND));

        if(!post.getUser().equals(user))
            throw new UserErrorHandler(ErrorCode._POST_FORBIDDEN);

        post.setTitle(request.getTitle());
        post.setCapacity(request.getCapacity());
        post.setDate(request.getDate());
        post.setCategory(request.getCategory());
        post.setPrice(request.getPrice());
        post.setPlace(request.getPlace());
        post.setDetailed_category(request.getDetailed_category());
        post.setContent(request.getContent());
        post.setRegionCode(user.getRegionCode());
        post.setClosed(false);
        post.setPoint(userService.convertPoint(request.getLongitude(), request.getLatitude()));

        return postRepository.save(post);
    }

    // 게시글 상세 조회
    @Override
    public PostResponseDTO.detailPostDTO detailPost(Long postId) {
        User user = userService.getLoggedInUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostErrorHandler(ErrorCode._POST_NOT_FOUND));
        return convertDetailPost(post, user);
    }

    // 공구 참여 신청
    @Override
    public PostResponseDTO.summaryPostDTO applicationPost(Long postId) {
        Post post = application(postId);
        return new PostResponseDTO.summaryPostDTO(post);
    }

    private Post application(Long postId) {
        User user = userService.getLoggedInUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostErrorHandler(ErrorCode._POST_NOT_FOUND));

        // 작성자 == 신청자, 마감된 공구
        if(post.getUser().equals(user) || post.isClosed()) {
            throw new PostErrorHandler(ErrorCode._POST_NOT_PARTICIPATE);
        }

        // 이미 참여한 공구
        if(participateRepository.existsByPostAndUser(post, user)) {
            throw new UserErrorHandler(ErrorCode._PARTICIPATE_CONFLICT);
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

    // 공구 마감
    @Override
    public PostResponseDTO.closePostDTO closePost(Long postId) {
        return new PostResponseDTO.closePostDTO(close(postId));
    }

    private Post close(Long postId) {
        User user = userService.getLoggedInUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostErrorHandler(ErrorCode._POST_NOT_FOUND));

        if(!post.getUser().equals(user))
            throw new PostErrorHandler(ErrorCode._POST_FORBIDDEN);

        // 해당 공구 신청자 -> 모두 거절
        participateService.closeRecruit(user, post);
        post.setClosed(true);
        return postRepository.save(post);

    }

    // 모집 게시글 조회
    @Override
    public List<PostResponseDTO.recruitDTO> findRecruitList() {
        List<Post> posts = getRecruitPosts();
        List<PostResponseDTO.recruitDTO> list = posts.stream()
                .map((Post p) -> convertRecruitPost(p))
                .collect(Collectors.toList());
        return list;
    }

    // 작성한 게시글 리스트
    @Override
    public List<PostResponseDTO.postDataDTO> postList() {
        List<Post> posts = getRecruitPosts();
        List<PostResponseDTO.postDataDTO> list = posts.stream()
                .map(PostResponseDTO.postDataDTO::new)
                .collect(Collectors.toList());
        return list;
    }

    private List<Post> getRecruitPosts() {
        User user = userService.getLoggedInUser();
        List<Post> posts = postRepository.findAllByUser(user);

        /*if(posts.isEmpty())
            throw new PostErrorHandler(ErrorCode._POST_BAD_REQUEST);*/

        return posts;

    }

    // 관심목록 게시글
    @Override
    public List<PostResponseDTO.postDataDTO> postLikeList() {
        List<Post> posts = getLikePosts();
        List<PostResponseDTO.postDataDTO> list = posts.stream()
                .map(PostResponseDTO.postDataDTO::new)
                .collect(Collectors.toList());
        return list;
    }

    private List<Post> getLikePosts() {
        User user = userService.getLoggedInUser();
        List<PostLikes> likes = likesRepository.findAllByUser(user);

        return likes.stream()
                .map(PostLikes::getPost)
                .collect(Collectors.toList());
    }

    // 검색
    @Override
    public List<PostResponseDTO.postDTO> searchPosts(String q) {
        User user = userService.getLoggedInUser();

        Set<Post> posts = new HashSet<>();
        posts.addAll(postRepository.searchByKeyword(q));
        posts.addAll(postRepository.searchByKeywordNative(q));

        return posts.stream()
                .map((Post p) -> convertPost(p, user))
                .collect(Collectors.toList());
    }

    // post 응답 객체 변환
    private PostResponseDTO.postDTO convertPost(Post p, User u) {
        int likes = Integer.parseInt(String.valueOf(likesRepository.countByPost(p)));
        boolean status;

        if(likesRepository.existsByPostAndUser(p, u)) {
            status = likesRepository.findByPostAndUser(p, u).isStatus();
        } else {
            status = false;
        }

        String upload = TimeUtil.convertTime(p.getUpdatedAt());
        PostResponseDTO.postDTO post = new PostResponseDTO.postDTO(p, likes, status, upload);

        return post;
    }

    private PostResponseDTO.detailPostDTO convertDetailPost(Post p, User u) {
        //detailPostDTO(Post p, String upload, int likes, boolean like_status, boolean isAuthor, boolean isJoin)
        int likes = Integer.parseInt(String.valueOf(likesRepository.countByPost(p)));
        boolean status;

        if(likesRepository.existsByPostAndUser(p, u)) {
            status = likesRepository.findByPostAndUser(p, u).isStatus();
        } else {
            status = false;
        }

        String upload = TimeUtil.convertTime(p.getCreatedAt());

        boolean isAuthor = p.getUser().equals(u);
        boolean isJoin = participateRepository.existsByPostAndUser(p, u);

        return new PostResponseDTO.detailPostDTO(p, upload, likes, status, isAuthor, isJoin);
    }

    private PostResponseDTO.recruitDTO convertRecruitPost(Post p) {
        int number = Integer.parseInt(String.valueOf(participateRepository.countAllByPost(p)));

        PostResponseDTO.recruitDTO post = new PostResponseDTO.recruitDTO(p.getTitle(), number, p.getId());
        return post;
    }
}
