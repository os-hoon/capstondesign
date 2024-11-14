package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.ChatMessage;
import cherrysumer.cherrysumer.domain.ChatRoom;
import cherrysumer.cherrysumer.domain.User;
import cherrysumer.cherrysumer.exception.BaseException;
import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.repository.ChatRoomRepository;
import cherrysumer.cherrysumer.repository.UserRepository;
import cherrysumer.cherrysumer.web.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService{

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Override
    public ChatMessage createChatMessage(ChatMessageDTO chatMessageDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode._USER_NOT_FOUND));
        ChatMessage chatMessage = chatMessageDto.toEntity(user.getId());
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessage.getRoomId()).orElseThrow();
        chatRoom.setLastChatMesg(chatMessage);
        chatRoomRepository.save(chatRoom);

        return chatMessage;
    }
}
