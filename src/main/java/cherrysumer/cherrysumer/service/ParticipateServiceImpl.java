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
    public List<PostResponseDTO.applicationDTO> findApplicationList() {
        return getApplicationPosts();
    }

    private List<PostResponseDTO.applicationDTO> getApplicationPosts() {
        User user = userService.getLoggedInUser();
        List<Participate> participates = participateRepository.findAllByUser(user);

        /*if(participates.isEmpty())
            throw new PostErrorHandler(ErrorCode._POST_BAD_REQUEST);*/

        List<PostResponseDTO.applicationDTO> list = participates.stream()
                .map((Participate p) -> convertApplicationPost(p))
                .collect(Collectors.toList());

        return list;
    }

    // 참여 신청자 목록 조회
    @Override
    public List<PostResponseDTO.participateUserDTO> participateList(Long postId, int status) {
        // 0 : 전체, 1: 승인, 2: 거절
        if(status != 0 && status != 1 && status != 2) {
            throw new BaseException(ErrorCode._BAD_REQUEST);
        }

        List<Participate> participates = participate(postId, status);

        List<PostResponseDTO.participateUserDTO> list = participates.stream()
                .map((Participate p) -> convertUser(p))
                .collect(Collectors.toList());

        return list;
    }

    public List<Participate> participate(Long postId, int status) {
        User user = userService.getLoggedInUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostErrorHandler(ErrorCode._POST_NOT_FOUND));

        if(!post.getUser().equals(user)) {
            throw new PostErrorHandler(ErrorCode._POST_FORBIDDEN);
        }

        List<Participate> participates;

        if(status == 0) {
            participates = participateRepository.findAllByPost0(post);
        } else if(status == 1) {
            participates = participateRepository.findAllByPost1(post);
        } else {
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

        // 신청 유저 == 작성자
        if(!post.getUser().equals(author))
            throw new PostErrorHandler(ErrorCode._POST_FORBIDDEN);
        // 마감된 모집, 승인한 사용자
        if(post.isClosed() && request.getIsConfirmed() == 0)
            throw new PostErrorHandler(ErrorCode._POST_CONFLICT);
        // 값이 0,1이 아닌 경우
        if(request.getIsConfirmed() != 0 && request.getIsConfirmed() != 1) {
            throw new BaseException(ErrorCode._BAD_REQUEST);
        }

        Participate participate = participateRepository.findByPostAndUser(post, user)
                .orElseThrow(() -> new BaseException(ErrorCode._PARTICIPATE_NOT_FOUND));

        participate.setStatus(request.getIsConfirmed());
        participateRepository.save(participate);
        // 모집 정원이 다 찬 경우
        if(participateRepository.countParticipateByPost(post) == post.getCapacity()) {
            closeRecruit(user, post); // 모집 마감
            post.setClosed(true);
            postRepository.save(post);
        }
    }

    // 모집 마감 -> 신청 사용자 모두 거절
    @Override
    public void closeRecruit(User user, Post post) {
        for(Participate p : participateRepository.searchByConfirm(post)) {
            p.setStatus(1);
            participateRepository.save(p);
        }
    }

    // 응답 객체 변환
    private PostResponseDTO.applicationDTO convertApplicationPost(Participate p) {
        PostResponseDTO.applicationDTO post =
                new PostResponseDTO.applicationDTO(p.getPost().getTitle(), p.getPost().getId(), p.getStatus());
        return post;
    }

    private PostResponseDTO.participateUserDTO convertUser(Participate p) {
        PostResponseDTO.participateUserDTO user =
                new PostResponseDTO.participateUserDTO(p.getUser().getId(), p.getPost().getId(), p.getUser().getNickname(),
                        p.getUser().getRegion(), p.getStatus());
        return user;
    }
}
