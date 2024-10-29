package cherrysumer.cherrysumer.web.controller;

import cherrysumer.cherrysumer.domain.ChatMessage;
import cherrysumer.cherrysumer.service.ChatMessageService;
import cherrysumer.cherrysumer.web.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/message")
    public void sendMessage(ChatMessageDTO message) {
        ChatMessage newChat = chatMessageService.createChatMessage(message);
        log.info("received message: {}", message);
        messagingTemplate.convertAndSend("/sub/channel/" + message.getRoomId(), newChat);
    }
}
