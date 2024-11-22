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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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
    private final ImageUploadService imageUploadService;

    // 게시글 저장
    @Override
    public PostResponseDTO.summaryPostDTO savePost(PostRequestDTO.postDTO request, List<MultipartFile> imagefile) throws ParseException, IOException {
        Post post = save(request);
        imageUpload(imagefile, post);
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
        post.setRegion(user.getRegion());
        post.setRegionCode(user.getRegionCode());
        post.setClosed(false);
        post.setClosed(false);
        post.setPoint(userService.convertPoint(request.getLongitude(), request.getLatitude()));
        post.setPostImage(new ArrayList<>());

        postRepository.save(post);
        return application(post.getId(), "게시자");
    }

    private Post imageUpload(List<MultipartFile> imagefile, Post post) throws IOException {
        // 저장할 이미지 리스트
        List<Post.Image> imagePaths = new ArrayList<>();

        // 기존 이미지 가져오기
        List<String> originImage = post.getPostImage().stream()
                .map(Post.Image::getOriginfilename)
                .collect(Collectors.toList());

        for(MultipartFile file : imagefile) {
            // 빈파일 인 경우 넘기기
            if(file == null || file.isEmpty())
                continue;

            String originalFilename = file.getOriginalFilename(); // 파일 이름
            if(!originImage.contains(originalFilename)) { // 기존 리스트에 포함 x 파일인 경우 -> 새로 추가
                String newFileName = imageUploadService.uploadImage(file);
                Post.Image imagePath = new Post.Image(originalFilename, newFileName);
                imagePaths.add(imagePath);
            } else { // 기존에 있는 파일인 경우 -> 그대로 저장
                int idx = originImage.indexOf(originalFilename);
                imagePaths.add(post.getPostImage().get(idx));
            }
        }

        post.setPostImage(imagePaths);

        return postRepository.save(post);
    }

    // 게시글 조회
    @Override
    public PostResponseDTO.postListDTO findRegionPosts(List<String> category, String filter) {
        return getRegionPosts(category, filter);
    }

    /***
     * @param category: 카테고리 필터 (9 + 무선택)
     * @param filter: 최신순, 추천순, 인기순, 저가순, 고가순
     * @return
     */
    private PostResponseDTO.postListDTO getRegionPosts(List<String> category, String filter) {
        User user = userService.getLoggedInUser();

        List<Post> allposts = postRepository.findAllByRegionCode(user.getRegionCode());
        List<Post> posts = allposts;
        if(category != null ) {
            posts = allposts.stream()
                    .filter(post -> post.getCategory() != null &&
                            post.getCategory().stream().anyMatch(cat -> category.contains(cat)))
                    .collect(Collectors.toList());
        }
                /*(category == null) ? postRepository.findAllByRegionCode(user.getRegionCode()) :
                postRepository.findAllPost(user.getRegionCode(), category);*/

        posts = filterPost(user, posts, filter);

        List<PostResponseDTO.postDTO> list = posts.stream()
                .map(p -> convertPost(p, user))
                .collect(Collectors.toList());

        return new PostResponseDTO.postListDTO(user.getRegion(), user.getNickname(), list);
    }

    private List<Post> filterPost(User user, List<Post> posts, String filter) {
        List<String> userCate = user.getCategory();

        switch (filter) {
            case "최신순":
                posts.sort(Comparator.comparing(Post::getCreatedAt).reversed());
                break;
            case "추천순":
                posts = posts.stream()
                        .filter(p -> p.getCategory().stream()
                                .anyMatch(userCate::contains)) // userCate에 포함된 값만 필터링
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
    @Transactional
    public void deletePost(Long postId) {
        delete(postId);
    }

    //@Transactional
    private void delete(Long postId) {
        User user = userService.getLoggedInUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostErrorHandler(ErrorCode._POST_NOT_FOUND));

        if(post.getUser().equals(user)) {
            participateRepository.deleteAllparticipate(post);
            likesRepository.deleteAlllikes(post);
            postRepository.delete(post);
        } else
            throw new PostErrorHandler(ErrorCode._POST_FORBIDDEN);
    }

    // 게시글 수정
    @Override
    public PostResponseDTO.detailPostDTO updatePost(PostRequestDTO.postDTO request, List<MultipartFile> imagefile) throws ParseException, IOException {
        User user = userService.getLoggedInUser();
        Post post = update(request, user);
        post = imageUpload(imagefile, post);
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
        //post.setRegionCode(user.getRegionCode());
        //post.setClosed(false);
        //post.setPoint(userService.convertPoint(request.getLongitude(), request.getLatitude()));

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
        Post post = application(postId, "미확인");
        return new PostResponseDTO.summaryPostDTO(post);
    }

    private Post application(Long postId, String status) {
        User user = userService.getLoggedInUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostErrorHandler(ErrorCode._POST_NOT_FOUND));

        // 작성자 == 신청자, 마감된 공구
        if((post.getUser().equals(user) && !status.equals("게시자")) || post.isClosed()) {
            throw new PostErrorHandler(ErrorCode._POST_NOT_PARTICIPATE);
        }

        // 이미 참여한 공구
        if(participateRepository.existsByPostAndUser(post, user)) {
            throw new UserErrorHandler(ErrorCode._PARTICIPATE_CONFLICT);
        }

        Participate p = new Participate();
        p.setUser(user);
        p.setPost(post);
        p.setStatus(status);
        p.setRegistered(false);
        participateRepository.save(p);

        return post;
    }

    // 게시글 관심 목록 추가, 삭제
    @Override
    public PostResponseDTO.likePostDTO likePost(Long postId) {
        return likes(postId);
        //boolean like = likes(postId);
        //return new PostResponseDTO.likePostDTO(postId, like);
    }

    private PostResponseDTO.likePostDTO likes(Long postId) {
        User user = userService.getLoggedInUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostErrorHandler(ErrorCode._POST_NOT_FOUND));

        boolean status;
        if(likesRepository.existsByPostAndUser(post, user)) {
            PostLikes like = likesRepository.findByPostAndUser(post,user);
            //like.setStatus(!like.isStatus());
            likesRepository.delete(like);
            status = false;
        } else {
            PostLikes like = new PostLikes();
            like.setPost(post);
            like.setUser(user);
            likesRepository.save(like);
            status = true;
            //like.setStatus(true);
        }

        int likes = likesRepository.countByPost(post).intValue();
        return new PostResponseDTO.likePostDTO(postId, status, likes);
        //likesRepository.save(like);
        //return like;
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
        participateService.closeRecruit(post);
        post.setClosed(true);
        return postRepository.save(post);

    }

    // 모집 게시글 조회
    @Override
    public List<PostResponseDTO.participateDTO> findRecruitList() {
        User user = userService.getLoggedInUser();
        List<Post> posts = getRecruitPosts(user);
        List<PostResponseDTO.participateDTO> list = posts.stream()
                .map((Post p) -> convertRecruitPost(p, user))
                .collect(Collectors.toList());
        return list;
    }

    // 작성한 게시글 리스트
    @Override
    public List<PostResponseDTO.postDataDTO> postList() {
        User user = userService.getLoggedInUser();
        List<Post> posts = getRecruitPosts(user);
        List<PostResponseDTO.postDataDTO> list = posts.stream()
                .map(p -> convertPostDataDTO(p))
                .collect(Collectors.toList());
        return list;
    }

    private List<Post> getRecruitPosts(User user) {
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
                .map(p -> convertPostDataDTO(p))
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

        List<Post> allposts = postRepository.findAllByRegionCode(user.getRegionCode());
        List<Post> categoryPosts = allposts;
        if(category != null ) {
            categoryPosts = allposts.stream()
                    .filter(post -> post.getCategory() != null &&
                            post.getCategory().stream().anyMatch(cat -> category.contains(cat)))
                    .collect(Collectors.toList());
        }

        Set<Post> set = new HashSet<>();
        set.addAll(postRepository.searchByKeyword(q));
        set.addAll(postRepository.searchByKeywordNative(q));

        categoryPosts.retainAll(set);

        List<Post> posts = filterPost(user, categoryPosts, filter);

        return posts.stream()
                .map((Post p) -> convertPost(p, user))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> recommendKeyword() {
        User user = userService.getLoggedInUser();
        System.out.println(user.getCategory().toString());
        List<Post> posts = filterPost(user, postRepository.findAllByRegionCode(user.getRegionCode()), "추천순");
        return posts.stream().map(p -> p.getProductname()).collect(Collectors.toList());
    }

    // post 응답 객체 변환
    private PostResponseDTO.postDTO convertPost(Post p, User u) {
        int likes = likesRepository.countByPost(p).intValue();
        boolean status = likesRepository.existsByPostAndUser(p, u);

        if(!p.isClosed() && (p.getDate().isBefore(LocalDateTime.now()))) {
            participateService.closeRecruit(p);
            p.setClosed(true);
            postRepository.save(p);
        }

        String upload = TimeUtil.convertTime(p.getCreatedAt());
        PostResponseDTO.postDTO post = new PostResponseDTO.postDTO(p, likes, status, upload);

        return post;
    }

    private PostResponseDTO.detailPostDTO convertDetailPost(Post p, User u) {
        //detailPostDTO(Post p, String upload, int likes, boolean like_status, boolean isAuthor, boolean isJoin)
        int likes = likesRepository.countByPost(p).intValue();
        boolean status = likesRepository.existsByPostAndUser(p, u);

        String upload = TimeUtil.convertTime(p.getCreatedAt());

        boolean isAuthor = p.getUser().equals(u);
        boolean isJoin = participateRepository.existsByPostAndUser(p, u);

        if(!p.isClosed() && (p.getDate().isBefore(LocalDateTime.now()))) {
            participateService.closeRecruit(p);
            p.setClosed(true);
            postRepository.save(p);
        }

        return new PostResponseDTO.detailPostDTO(p, upload, likes, status, isAuthor, isJoin);
    }

    private PostResponseDTO.participateDTO convertRecruitPost(Post p, User u) {
        int number = Integer.parseInt(String.valueOf(participateRepository.countAllByPost(p)));

        Participate participate = participateRepository.findByPostAndUser(p, u)
                .orElseThrow(() -> new BaseException(ErrorCode._PARTICIPATE_NOT_FOUND));
        PostResponseDTO.participateDTO post = new PostResponseDTO.participateDTO(p, number, participate.isRegistered());
        return post;
    }

    private PostResponseDTO.postDataDTO convertPostDataDTO(Post p) {
        if(!p.isClosed() && (p.getDate().isBefore(LocalDateTime.now()))) {
            participateService.closeRecruit(p);
            p.setClosed(true);
            postRepository.save(p);
        }
        return new PostResponseDTO.postDataDTO(p);
    }
}
