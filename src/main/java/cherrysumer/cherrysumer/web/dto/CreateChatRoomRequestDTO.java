package cherrysumer.cherrysumer.web.dto;

import lombok.Getter;

@Getter
public class CreateChatRoomRequestDTO {
    private Long roomMakerId;
    private Long guestId;
    private Long postId; // 추가된 필드
}
