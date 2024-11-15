package cherrysumer.cherrysumer.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ChatRoomMemberDTO {
    private String chatRoomId;
    private Long userId;        // User ID
    private String userNickname; // User Nickname
    private Long postId;        // Post ID
    private String userProfileImageUrl;  // User의 프로필 이미지 URL
    private String lastMessage;          // 마지막 메시지
    private LocalDateTime updatedAt;     // 메시지의 마지막 업데이트 시간
    private String status;  // Participate의 상태
}
