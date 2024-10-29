package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.web.dto.CreateChatRoomRequestDTO;
import cherrysumer.cherrysumer.web.dto.CreateChatRoomResponseDTO;

public interface ChatRoomService {

    CreateChatRoomResponseDTO createChatRoomForPersonal(CreateChatRoomRequestDTO chatRoomRequest);
}
