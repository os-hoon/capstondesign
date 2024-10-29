package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.ChatRoom;
import cherrysumer.cherrysumer.domain.User;
import cherrysumer.cherrysumer.exception.BaseException;
import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.repository.ChatRoomRepository;
import cherrysumer.cherrysumer.repository.UserRepository;
import cherrysumer.cherrysumer.web.dto.CreateChatRoomRequestDTO;
import cherrysumer.cherrysumer.web.dto.CreateChatRoomResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;

    @Override // 개인 DM방 생성
    public CreateChatRoomResponseDTO createChatRoomForPersonal(CreateChatRoomRequestDTO chatRoomRequest) {

        User roomMaker = userService.getLoggedInUser();

        User guest = userRepository.findById(chatRoomRequest.getGuestId())
                .orElseThrow(() -> new BaseException(ErrorCode._USER_NOT_FOUND));

        ChatRoom newRoom  = ChatRoom.create();
        newRoom.addMembers(roomMaker, guest);

        chatRoomRepository.save(newRoom);

        return new CreateChatRoomResponseDTO(roomMaker.getId(),guest.getId(), newRoom.getId());
    }
}
