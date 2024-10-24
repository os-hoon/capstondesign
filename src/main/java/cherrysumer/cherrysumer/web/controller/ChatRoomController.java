package cherrysumer.cherrysumer.web.controller;

import cherrysumer.cherrysumer.util.ApiResponse;
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
    public ApiResponse<ChatDto.CreateChatRoomResponse> createPersonalChatRoom(@RequestBody ChatDto.CreateChatRoomRequest request) {
        return ApiResponse.onSuccess(chatRoomService.createChatRoomForPersonal(request));
    }
}