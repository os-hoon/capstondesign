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
public class ParticipateServiceImpl implements ParticipateService{

    private final PostRepository postRepository;
    private final PostLikesRepository likesRepository;
    private final UserService userService;
    private final ParticipateRepository participateRepository;


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

    // 참여 신청자 목록 조회
    @Override
    public List<PostResponseDTO.participateUserDTO> participateList(Long postId) {
        List<Participate> participates = participate(postId);
        List<PostResponseDTO.participateUserDTO> list = participates.stream()
                .map((Participate p) -> convertUser(p))
                .collect(Collectors.toList());

        return list;
    }

    public List<Participate> participate(Long postId) {
        User user = userService.getLoggedInUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostErrorHandler(ErrorCode._POST_NOT_FOUND));

        if(!post.getUser().equals(user)) {
            throw new PostErrorHandler(ErrorCode._POST_FORBIDDEN);
        }

        List<Participate> participates = participateRepository.findAllByPost(post);
        return participates;
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

    private PostResponseDTO.participateUserDTO convertUser(Participate p) {
        PostResponseDTO.participateUserDTO user =
                new PostResponseDTO.participateUserDTO(p.getUser().getId(), p.getPost().getId(),
                        p.getUser().getRegion(), p.getStatus());
        return user;
    }
}
