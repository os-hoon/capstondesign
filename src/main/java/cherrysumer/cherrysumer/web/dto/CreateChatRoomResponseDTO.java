package cherrysumer.cherrysumer.web.dto;

import lombok.Getter;

@Getter
public class CreateChatRoomResponseDTO {
    private String roomMakerId;
    private String guestId;
    private String chatRoomId;

    /* Entity -> Dto */
    public CreateChatRoomResponseDTO(String roomMakerId, String guestId, String chatRoomId) {
        this.roomMakerId = roomMakerId;
        this.guestId = guestId;
        this.chatRoomId = chatRoomId;
    }
}
