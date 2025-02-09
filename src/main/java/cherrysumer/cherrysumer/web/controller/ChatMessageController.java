package cherrysumer.cherrysumer.web.controller;

import cherrysumer.cherrysumer.domain.ChatMessage;
import cherrysumer.cherrysumer.service.ChatMessageService;
import cherrysumer.cherrysumer.web.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/message")
    public void sendMessage(ChatMessageDTO message, SimpMessageHeaderAccessor accessor) {
        String userId = (String) accessor.getSessionAttributes().get("userId");
        Long userIdAsLong = Long.parseLong(userId);
        ChatMessageDTO.chatMessageDTO newChat = chatMessageService.createChatMessage(message,userIdAsLong);
        log.info("received message: {}", message);
        messagingTemplate.convertAndSend("/sub/channel/" + message.getRoomId(), newChat);
    }

    @SubscribeMapping("/channel/{roomId}")
    public ChatMessageDTO.chatMessageListDTO test(@DestinationVariable("roomId") String roomId, SimpMessageHeaderAccessor accessor) {
        String userId = (String) accessor.getSessionAttributes().get("userId");
        return chatMessageService.messageList(roomId, userId);
    }
}
