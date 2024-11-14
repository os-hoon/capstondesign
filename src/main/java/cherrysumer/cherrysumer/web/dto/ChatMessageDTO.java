package cherrysumer.cherrysumer.web.dto;

import cherrysumer.cherrysumer.domain.ChatMessage;
import cherrysumer.cherrysumer.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private String roomId;
    private String message;


    /* Dto -> Entity */
    public ChatMessage toEntity(Long userId) {
        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(roomId)
                .message(message)
                .user(userId)
                .build();
        return chatMessage;
    }
}
