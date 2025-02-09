package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.Participate;
import cherrysumer.cherrysumer.domain.Post;
import cherrysumer.cherrysumer.domain.User;
import cherrysumer.cherrysumer.exception.BaseException;
import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.exception.handler.PostErrorHandler;
import cherrysumer.cherrysumer.exception.handler.UserErrorHandler;
import cherrysumer.cherrysumer.repository.ParticipateRepository;
import cherrysumer.cherrysumer.repository.PostLikesRepository;
import cherrysumer.cherrysumer.repository.PostRepository;
import cherrysumer.cherrysumer.repository.UserRepository;
import cherrysumer.cherrysumer.web.dto.PostResponseDTO;
import cherrysumer.cherrysumer.web.dto.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipateServiceImpl implements ParticipateService{

    private final PostRepository postRepository;
    private final PostLikesRepository likesRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ParticipateRepository participateRepository;

    // 참여 신청 게시글 조회
    @Override
    public List<PostResponseDTO.participateDTO> findApplicationList() {
        return getApplicationPosts();
    }

    private List<PostResponseDTO.participateDTO> getApplicationPosts() {
        User user = userService.getLoggedInUser();
        List<Participate> participates = participateRepository.findAllByUser(user);

        /*if(participates.isEmpty())
            throw new PostErrorHandler(ErrorCode._POST_BAD_REQUEST);*/

        List<PostResponseDTO.participateDTO> list = participates.stream()
                .map((Participate p) -> convertApplicationPost(p))
                .collect(Collectors.toList());

        return list;
    }

    // 참여 신청자 목록 조회
    @Override
    public List<PostResponseDTO.participateUserDTO> participateList(Long postId, String filter) {
        User user = userService.getLoggedInUser();
        List<Participate> participates;
        if(filter.equals("전체") || filter.equals("승인") || filter.equals(("거절"))) {
            participates = participate(user, postId, filter);
        } else {
            throw new BaseException(ErrorCode._BAD_REQUEST);
        }

        List<PostResponseDTO.participateUserDTO> list = participates.stream()
                .map((Participate p) -> convertUser(user.getId(), p))
                .collect(Collectors.toList());

        return list;
    }

    public List<Participate> participate(User user, Long postId, String filter) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostErrorHandler(ErrorCode._POST_NOT_FOUND));

        if(!post.getUser().equals(user)) {
            throw new PostErrorHandler(ErrorCode._POST_FORBIDDEN);
        }

        List<Participate> participates;

        if(filter.equals("승인")) {
            participates = participateRepository.findAllByPost0(post);
        } else if(filter.equals("거절")) {
            participates = participateRepository.findAllByPost1(post);
        } else { // 전체
            participates = participateRepository.findAllByPost(post);
        }
        return participates;
    }

    // 신청자 승인, 거절
    @Override
    public void updateParticipate(UserRequestDTO.decideUserDTO request) {
        User author = userService.getLoggedInUser();

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserErrorHandler(ErrorCode._USER_NOT_FOUND));
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new PostErrorHandler(ErrorCode._POST_NOT_FOUND));

        // 요청 유저 != 작성자
        if(!post.getUser().equals(author))
            throw new PostErrorHandler(ErrorCode._POST_FORBIDDEN);
        // 마감된 모집, 승인한 사용자
        if(post.isClosed() && request.getIsConfirmed().equals("승인"))
            throw new PostErrorHandler(ErrorCode._POST_CONFLICT);
        // 값이 0,1이 아닌 경우
        if(request.getIsConfirmed().equals("승인") && request.getIsConfirmed().equals("거절")) {
            throw new BaseException(ErrorCode._BAD_REQUEST);
        }

        Participate participate = participateRepository.findByPostAndUser(post, user)
                .orElseThrow(() -> new BaseException(ErrorCode._PARTICIPATE_NOT_FOUND));

        participate.setStatus(request.getIsConfirmed());
        participateRepository.save(participate);
        // 모집 정원이 다 찬 경우
        if(participateRepository.countParticipateByPost(post) == post.getCapacity()) {
            closeRecruit(post); // 모집 마감
            post.setClosed(true);
            postRepository.save(post);
        }
    }

    // 모집 마감 -> 신청 사용자 모두 거절
    @Override
    public void closeRecruit(Post post) {
        for(Participate p : participateRepository.searchByConfirm(post)) {
            p.setStatus("거절");
            participateRepository.save(p);
        }
    }

    @Override
    public void registerInventory(Long postId) {
        User user = userService.getLoggedInUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostErrorHandler(ErrorCode._POST_NOT_FOUND));

        Participate participate = participateRepository.findByPostAndUser(post, user)
                .orElseThrow(() -> new BaseException(ErrorCode._PARTICIPATE_NOT_FOUND));

        participate.setRegistered(true);
        participateRepository.save(participate);
    }

    // 응답 객체 변환
    private PostResponseDTO.participateDTO convertApplicationPost(Participate p) {
        PostResponseDTO.participateDTO post =
                new PostResponseDTO.participateDTO(p.getPost(), p.getStatus(), p.isRegistered());
        return post;
    }

    private PostResponseDTO.participateUserDTO convertUser(Long myId, Participate p) {
        PostResponseDTO.participateUserDTO user =
                new PostResponseDTO.participateUserDTO(myId, p.getUser(), p.getPost(), p.getStatus());
        return user;
    }
}
