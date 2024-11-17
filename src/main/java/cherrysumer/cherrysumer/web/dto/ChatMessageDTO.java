package cherrysumer.cherrysumer.web.dto;

import cherrysumer.cherrysumer.domain.ChatMessage;
import cherrysumer.cherrysumer.domain.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
                .createdAt(LocalDateTime.now())
                .build();
        return chatMessage;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class chatMessageListDTO {
        private Long myId; // 내 userId
        private Long partnerId; // 상대방 userId
        private String roomId; // 채팅방 Id
        private PostResponseDTO.summaryPostDTO post;
        private List<chatMessageDTO> chatList; // 이전 대화 내용
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class chatMessageDTO {
        private Long id; //메세지 id
        private Long senderId; // 송신자 userId
        private String date; // 보낸 날짜
        private String time; // 보낸 시간
        private String message; // 보낸 메세지
    }
}
