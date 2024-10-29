package cherrysumer.cherrysumer.web.controller;

import cherrysumer.cherrysumer.service.ChatRoomService;
import cherrysumer.cherrysumer.util.ApiResponse;
import cherrysumer.cherrysumer.web.dto.CreateChatRoomRequestDTO;
import cherrysumer.cherrysumer.web.dto.CreateChatRoomResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/chatRoom/")
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/personal") //개인 DM 채팅방 생성
    public ApiResponse<CreateChatRoomResponseDTO> createPersonalChatRoom(@RequestBody CreateChatRoomRequestDTO request) {
        return ApiResponse.onSuccess(chatRoomService.createChatRoomForPersonal(request));
    }
}