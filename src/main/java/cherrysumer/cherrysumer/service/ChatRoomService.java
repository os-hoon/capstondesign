package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.web.dto.ChatRoomMemberDTO;
import cherrysumer.cherrysumer.web.dto.CreateChatRoomRequestDTO;
import cherrysumer.cherrysumer.web.dto.CreateChatRoomResponseDTO;

import java.util.List;

public interface ChatRoomService {

    CreateChatRoomResponseDTO createChatRoomForPersonal(CreateChatRoomRequestDTO chatRoomRequest);
    List<ChatRoomMemberDTO> findChatRoomsByStatus(String status);

}
