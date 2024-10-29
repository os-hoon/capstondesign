package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.ChatMessage;
import cherrysumer.cherrysumer.domain.ChatRoom;
import cherrysumer.cherrysumer.repository.ChatRoomRepository;
import cherrysumer.cherrysumer.web.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService{

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatMessage createChatMessage(ChatMessageDTO chatMessageDto) {
        ChatMessage chatMessage = chatMessageDto.toEntity();
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessage.getRoomId()).orElseThrow();
        chatRoom.setLastChatMesg(chatMessage);
        chatRoomRepository.save(chatRoom);

        return chatMessage;
    }
}
