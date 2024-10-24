package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.ChatRoom;
import cherrysumer.cherrysumer.domain.User;
import cherrysumer.cherrysumer.exception.BaseException;
import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.repository.UserRepository;
import cherrysumer.cherrysumer.web.dto.CreateChatRoomRequestDTO;
import cherrysumer.cherrysumer.web.dto.CreateChatRoomResponseDTO;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    @Override // 개인 DM방 생성
    public CreateChatRoomResponseDTO createChatRoomForPersonal(CreateChatRoomRequestDTO chatRoomRequest) {
        String id = securityUtil.getCurrentMemberUsername(); //id=roomMakerId 같아야 함
        if (!id.equals(chatRoomRequest.getRoomMakerId())) {
            throw UserNotFoundException.EXCEPTION;
        }
        User roomMaker = userService.getLoggedInUser();
        User guest = userRepository.findById(chatRoomRequest.getGuestId());

        ChatRoom newRoom  = ChatRoom.create();
        newRoom.addMembers(roomMaker, guest);

        chatRoomRepository.save(newRoom);

        return new ChatDto.CreateChatRoomResponse(roomMaker.getId(),guest.getId(), newRoom.getId());
    }
