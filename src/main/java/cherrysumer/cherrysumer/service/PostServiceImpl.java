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

import java.util.*;
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
        post.setProductname(request.getProductname());
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
    public List<PostResponseDTO.postDTO> findRegionPosts(List<String> category, String filter) {
        return getRegionPosts(category, filter);
    }

    /***
     * @param category: 카테고리 필터 (9 + 무선택)
     * @param filter: 최신순, 추천순, 인기순, 저가순, 고가순
     * @return
     */
    private List<PostResponseDTO.postDTO> getRegionPosts(List<String> category, String filter) {
        User user = userService.getLoggedInUser();

        List<Post> posts = (category == null) ? postRepository.findAllByRegionCode(user.getRegionCode()) :
                postRepository.findAllPost(user.getRegionCode(), category);

        posts = filterPost(user, posts, filter);
        //List<Post> posts = postRepository.findAllByRegionCodeOrderByCreatedAt(user.getRegionCode());
        //posts.sort((o1, o2) -> (int) (likesRepository.countByPost(o2) - likesRepository.countByPost(o1)));

        List<PostResponseDTO.postDTO> list = posts.stream()
                .map((Post p) -> convertPost(p, user))
                .collect(Collectors.toList());

        return list;
    }

    private List<Post> filterPost(User user, List<Post> posts, String filter) {
        List<String> userCate = user.getCategory();

        switch (filter) {
            case "최신순":
                posts.sort(Comparator.comparing(Post::getUpdatedAt).reversed());
                break;
            case "추천순":
                posts = posts.stream()
                        .sorted(Comparator.comparingInt(p -> userCate.contains(p.getCategory()) ? 0 : 1))
                        .collect(Collectors.toList());
                break;
            case "인기순":
                posts.sort((o1, o2) -> (int) (likesRepository.countByPost(o2) - likesRepository.countByPost(o1)));
                break;
            case "저가순":
                posts.sort((o1, o2) -> (int) ((o1.getPrice()/(double)o1.getCapacity()) - (o2.getPrice()/(double)o2.getCapacity())));
                break;
            case "고가순":
                posts.sort((o1, o2) -> (int) ((o2.getPrice()/(double)o2.getCapacity()) - (o1.getPrice()/(double)o1.getCapacity())));
                break;
            default:
                throw new BaseException(ErrorCode._POST_BAD_REQUEST);
        }

        return posts;
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
        p.setStatus("미확인");
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
    public List<PostResponseDTO.postDTO> searchPosts(String q, List<String> category, String filter) {
        User user = userService.getLoggedInUser();

        List<Post> sortPost = (category == null) ? postRepository.findAllByRegionCode(user.getRegionCode()) :
                postRepository.findAllPost(user.getRegionCode(), category);

        Set<Post> set = new HashSet<>();
        set.addAll(postRepository.searchByKeyword(q));
        set.addAll(postRepository.searchByKeywordNative(q));
        set.addAll(sortPost);

        List<Post> posts = filterPost(user, new ArrayList<>(set), filter);

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

        PostResponseDTO.recruitDTO post = new PostResponseDTO.recruitDTO(p.getId(), p.getTitle(), p.getProductname(), number);
        return post;
    }
}
