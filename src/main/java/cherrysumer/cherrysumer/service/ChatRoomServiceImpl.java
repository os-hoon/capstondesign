package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.ChatRoom;
import cherrysumer.cherrysumer.domain.Post;
import cherrysumer.cherrysumer.domain.User;
import cherrysumer.cherrysumer.exception.BaseException;
import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.repository.ChatRoomRepository;
import cherrysumer.cherrysumer.repository.PostRepository;
import cherrysumer.cherrysumer.repository.UserRepository;
import cherrysumer.cherrysumer.web.dto.CreateChatRoomRequestDTO;
import cherrysumer.cherrysumer.web.dto.CreateChatRoomResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final UserRepository userRepository;
    private final PostRepository postRepository; // 추가된 PostRepository
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;

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
}
