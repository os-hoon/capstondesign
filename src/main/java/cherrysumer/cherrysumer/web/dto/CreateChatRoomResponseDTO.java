package cherrysumer.cherrysumer.web.dto;

import lombok.Getter;

@Getter
public class CreateChatRoomResponseDTO {
    private Long roomMakerId;
    private Long guestId;
    private String chatRoomId;

    /* Entity -> Dto */
    public CreateChatRoomResponseDTO(Long roomMakerId, Long guestId, String chatRoomId) {
        this.roomMakerId = roomMakerId;
        this.guestId = guestId;
        this.chatRoomId = chatRoomId;
    }
}
