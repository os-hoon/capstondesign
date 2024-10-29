package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.ChatMessage;
import cherrysumer.cherrysumer.web.dto.ChatMessageDTO;

public interface ChatMessageService {
    ChatMessage createChatMessage(ChatMessageDTO chatMessageDto);
}
