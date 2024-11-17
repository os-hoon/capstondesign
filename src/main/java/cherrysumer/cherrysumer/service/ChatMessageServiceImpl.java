package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.*;
import cherrysumer.cherrysumer.exception.BaseException;
import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.repository.*;
import cherrysumer.cherrysumer.web.dto.ChatMessageDTO;
import cherrysumer.cherrysumer.web.dto.PostResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService{

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    public ChatMessageDTO.chatMessageDTO createChatMessage(ChatMessageDTO chatMessageDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode._USER_NOT_FOUND));
        ChatMessage chatMessage = chatMessageDto.toEntity(user.getId());
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessage.getRoomId()).orElseThrow();
        chatRoom.setLastChatMesg(chatMessage);
        chatRoomRepository.save(chatRoom);

        return convertMessage(chatMessage);
    }

    @Override
    public ChatMessageDTO.chatMessageListDTO messageList(String roomId, String userId) {
        // 채팅방 멤버 가져오기
        List<ChatRoomMember> chatRoom = memberRepository.findByChatRoomId(roomId);

        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new BaseException(ErrorCode._USER_NOT_FOUND));
        // 상대방 id 가져오기
        Long partner = chatRoom.stream()
                .filter(member -> !member.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new BaseException(ErrorCode._USER_NOT_FOUND))
                .getUser().getId();

        Long postId = chatRoom.stream()
                .map(member -> member.getPost())
                .findFirst()
                .get().getId();

        // 이전 채팅 내역 조회
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomId(roomId);
        chatMessages.sort(Comparator.comparingLong(ChatMessage::getId));
        // 메세지 응답 변환
        List<ChatMessageDTO.chatMessageDTO> message = chatMessages.stream()
                .map(m -> convertMessage(m))
                .collect(Collectors.toList());

        PostResponseDTO.summaryPostDTO p = new PostResponseDTO.summaryPostDTO(postRepository.findById(postId).orElse(null));
        return new ChatMessageDTO.chatMessageListDTO(user.getId(), partner, roomId, p, message);
    }

    private ChatMessageDTO.chatMessageDTO convertMessage(ChatMessage message) {
        String date = message.getCreatedAt() == null ? null : message.getCreatedAt().toLocalDate().toString();
        String time = message.getCreatedAt() == null ? null : message.getCreatedAt().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return new ChatMessageDTO.chatMessageDTO(message.getId(), message.getUser(), date, time, message.getMessage());
    }
}
