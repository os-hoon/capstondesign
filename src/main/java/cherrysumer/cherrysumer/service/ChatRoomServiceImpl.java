package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.*;
import cherrysumer.cherrysumer.exception.BaseException;
import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.repository.*;
import cherrysumer.cherrysumer.web.dto.ChatRoomMemberDTO;
import cherrysumer.cherrysumer.web.dto.CreateChatRoomRequestDTO;
import cherrysumer.cherrysumer.web.dto.CreateChatRoomResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final UserRepository userRepository;
    private final PostRepository postRepository; // 추가된 PostRepository
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;
    private final ParticipateRepository participateRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Override // 개인 DM방 생성
    public CreateChatRoomResponseDTO createChatRoomForPersonal(CreateChatRoomRequestDTO chatRoomRequest) {

        User roomMaker = userService.getLoggedInUser();

        User guest = userRepository.findById(chatRoomRequest.getGuestId())
                .orElseThrow(() -> new BaseException(ErrorCode._USER_NOT_FOUND));

        Post post = postRepository.findById(chatRoomRequest.getPostId())
                .orElseThrow(() -> new BaseException(ErrorCode._POST_NOT_FOUND)); // Post 검증 추가

        ChatRoom newRoom  = ChatRoom.create();
        newRoom.addMember(roomMaker, post); // 방 생성자와 포스트 추가
        newRoom.addMember(guest, post);    // 게스트와 포스트 추가

        chatRoomRepository.save(newRoom);

        return new CreateChatRoomResponseDTO(roomMaker.getId(),guest.getId(), newRoom.getId(),post.getId());
    }

    @Override
    public List<ChatRoomMemberDTO> findChatRoomsByStatus(String status) {
        // 로그인된 사용자 가져오기
        User loggedInUser = userService.getLoggedInUser();

        // 특정 메서드를 통해 로그인한 사용자의 chatRoomIds 가져오기
        List<String> chatRoomIds = getChatRoomIdsForLoggedInUser(loggedInUser);

        // chatRoomId로 데이터를 조회
        List<ChatRoomMember> filteredMembers = chatRoomIds.stream()
                .flatMap(chatRoomId -> chatRoomMemberRepository.findByChatRoomId(chatRoomId).stream())
                .toList();

        // DTO로 변환
        return filteredMembers.stream()
                .map(member -> {
                    // Participate 엔티티에서 User와 Post에 따른 상태 조회
                    Participate participate = participateRepository.findByPostAndUser(member.getPost(), member.getUser()).orElse(null);

                    // DTO 생성
                    return ChatRoomMemberDTO.builder()
                            .chatRoomId(member.getChatRoom().getId())
                            .userId(member.getUser().getId())
                            .userNickname(member.getUser().getNickname())
                            .postId(member.getPost().getId())
                            .userProfileImageUrl(member.getUser().getProfileImageUrl())
                            .lastMessage(member.getChatRoom().getLastChatMesg().getMessage() != null ? member.getChatRoom().getLastChatMesg().getMessage() : " ")
                            .updatedAt(member.getChatRoom().getLastChatMesg().getCreatedAt())
                            .status(participate != null ? participate.getStatus() : "unknown")
                            .build();
                })
                .filter(dto -> {
                    // 로그인한 사용자 자신을 제외
                    if (dto.getUserId().equals(loggedInUser.getId())) {
                        return false;
                    }

                    // status에 따른 데이터 필터링
                    if ("전체".equals(status)) {
                        return true; // 모든 데이터를 반환
                    } else if ("참여".equals(status)) {
                        return "승인".equals(dto.getStatus()) || "거절".equals(dto.getStatus()) || "미확인".equals(dto.getStatus());
                    } else if ("모집".equals(status)) {
                        return "게시자".equals(dto.getStatus());
                    }
                    return false; // 잘못된 status는 빈 리스트 반환
                })
                .toList();
    }

    private List<String> getChatRoomIdsForLoggedInUser(User loggedInUser) {
        // 로그인한 사용자의 ChatRoomMember 정보 가져오기
        List<ChatRoomMember> userChatRoomMembers = chatRoomMemberRepository.findByUser(loggedInUser);

        // chatRoomId 추출 및 반환
        return userChatRoomMembers.stream()
                .map(member -> member.getChatRoom().getId())
                .distinct() // 중복 제거
                .toList();
    }
}
