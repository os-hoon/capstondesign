package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.ChatMessage;
import cherrysumer.cherrysumer.web.dto.ChatMessageDTO;

public interface ChatMessageService {
    ChatMessageDTO.chatMessageDTO createChatMessage(ChatMessageDTO chatMessageDto, Long userId);
    ChatMessageDTO.chatMessageListDTO messageList(String roomId, String userId);
}
