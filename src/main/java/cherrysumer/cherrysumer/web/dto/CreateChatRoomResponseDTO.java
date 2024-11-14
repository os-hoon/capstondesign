package cherrysumer.cherrysumer.web.dto;

import lombok.Getter;

@Getter
public class CreateChatRoomResponseDTO {
    private Long roomMakerId;
    private Long guestId;
    private String chatRoomId;
    private Long postId; // 선택적으로 추가 가능

    /* Entity -> Dto */
    public CreateChatRoomResponseDTO(Long roomMakerId, Long guestId, String chatRoomId, Long postId) {
        this.roomMakerId = roomMakerId;
        this.guestId = guestId;
        this.chatRoomId = chatRoomId;
        this.postId = postId;
    }
}
