package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.*;
import cherrysumer.cherrysumer.exception.BaseException;
import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.repository.*;
import cherrysumer.cherrysumer.util.jwt.TokenProvider;
import cherrysumer.cherrysumer.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ImageUploadService imageUploadService;
    private final InventoryRepository inventoryRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final PostLikesRepository likesRepository;
    private final ParticipateRepository participateRepository;
    private final PostRepository postRepository;

    @Override
    public ProfileDTO.Extended getProfile() {
        // 사용자 찾기
        User user = userService.getLoggedInUser();

        // 프로필 정보 반환: /image/view/{fileName} 형식으로 프로필 이미지 URL 제공
        String imageUrl = user.getProfileImageUrl() != null ? "/image/view/" + user.getProfileImageUrl() : null;

        // 프로필 정보 반환
        return new ProfileDTO.Extended(user.getName(), user.getNickname(), user.getEmail(), user.getRegion(),imageUrl, user.getLoginId());
    }

    //수정하기 들어가서 아무것도 입력안하면 원래이름 닉네임 이메일 그대로 할거면 예외코드는 따로 설정 안할 예정
    @Override
    public ProfileDTO modifyProfile(ProfileDTO profileDTO, MultipartFile file) {

        try {
            // 사용자 찾기
            User user = userService.getLoggedInUser();

            // 프로필 수정
            user.setName(profileDTO.getName());
            user.setNickname(profileDTO.getNickname());
            user.setEmail(profileDTO.getEmail());


            // 프로필 이미지 파일이 비어 있지 않은 경우에만 업데이트
            if (file != null && !file.isEmpty()) {
                // 파일 업로드 처리
                String filePath = imageUploadService.uploadImage(file);

                // 프로필 이미지 경로 업데이트
                user.setProfileImageUrl(filePath);
            };
            userRepository.save(user);
            String imageUrl = user.getProfileImageUrl() != null ? "/image/view/" + user.getProfileImageUrl() : null;

            // 수정된 정보로 ProfileDTO 생성하여 반환
            return new ProfileDTO(user.getName(), user.getNickname(), user.getEmail(), user.getRegion(), imageUrl);


        } catch (IOException e) {
            return new ProfileDTO("Error", "Error", "Error","Error", null);
        }


    }

    @Override
    public RegionResponseDTO.successregionDTO setRegion(RegionDTO request)throws ParseException {
        // 사용자 찾기
        User user = userService.getLoggedInUser();

        user.setRegion(request.getRegion());
        user.setRegionCode(request.getRegionCode());
        user.setPoint(convertPoint(request.getLongitude(), request.getLatitude()));
        userRepository.save(user);


        return new RegionResponseDTO.successregionDTO(user.getRegion());

    }

    @Override
    public Point convertPoint(String longitude, String latitude) throws ParseException {
        if(longitude == null || longitude.equals("") || latitude == null || latitude.equals(""))
            return null;

        Double lng = Double.parseDouble(longitude);
        Double lti = Double.parseDouble(latitude);

        String pointWKT = String.format("POINT(%s %s)", lng, lti);
        Point point = (Point) new WKTReader().read(pointWKT);

        return point;
    }

    @Override
    @Transactional
    public void deleteUser(){
        User user = userService.getLoggedInUser();

        List<Inventory> inventories= inventoryRepository.findAllByUserId(user.getId());


        inventoryRepository.deleteAll(inventories);


        // 로그인한 사용자의 ChatRoomMember 정보 가져오기
        List<ChatRoomMember> userChatRoomMembers = chatRoomMemberRepository.findByUser(user);

        List<String> chatRoomIds = userChatRoomMembers.stream()
                .map(member -> member.getChatRoom().getId())
                .distinct() // 중복 제거
                .toList();


        for (String chatRoomId: chatRoomIds) {

            // 2. 해당 ChatRoom에 포함된 모든 ChatRoomMember 삭제
            List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository.findByChatRoomId(chatRoomId);
            chatRoomMemberRepository.deleteAll(chatRoomMembers);

            // 3. ChatRoom 삭제
            Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findById(chatRoomId);
            if (chatRoomOptional.isPresent()) {
                ChatRoom chatRoom = chatRoomOptional.get();
                chatRoomRepository.delete(chatRoom);
            }

            // 1. 해당 ChatRoom에 포함된 모든 ChatMessage 삭제
            List<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomId(chatRoomId);
            chatMessageRepository.deleteAll(chatMessages);

        }

        // 좋아요한 게시글 삭제
        likesRepository.deleteAlllikesUser(user);

        // 참여한 게시글 삭제
        participateRepository.deleteAllUser(user);

        // 게시글 삭제
        postRepository.deleteAllByUser(user);

        // 계정 삭제
        userRepository.delete(user);

    }


}
