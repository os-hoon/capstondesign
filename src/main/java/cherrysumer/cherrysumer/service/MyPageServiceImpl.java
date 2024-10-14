package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.User;
import cherrysumer.cherrysumer.exception.BaseException;
import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.repository.UserRepository;
import cherrysumer.cherrysumer.util.ApiResponse;
import cherrysumer.cherrysumer.web.dto.ProfileDTO;
import cherrysumer.cherrysumer.web.dto.RegionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final UserRepository userRepository;
    private final ImageUploadService imageUploadService;

    @Override
    public ProfileDTO getProfile(Long userId) {
        // 사용자 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode._USER_NOT_FOUND));

        // 프로필 정보 반환
        return new ProfileDTO(user.getName(), user.getNickname(), user.getEmail(), user.getRegion());
    }

    //수정하기 들어가서 아무것도 입력안하면 원래이름 닉네임 이메일 그대로 할거면 예외코드는 따로 설정 안할 예정
    @Override
    public void modifyProfile(Long userId, ProfileDTO profileDTO, MultipartFile file) {

        try {
            // 사용자 찾기
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new BaseException(ErrorCode._USER_NOT_FOUND));

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


        } catch (IOException e) {

        }


    }

    //동네설정은 아직 피그마에도 안나와서 일단 이런식으로 임시로 만들어둠
    @Override
    public void setRegion(Long userId, RegionDTO regionDTO) {
        // 사용자 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode._USER_NOT_FOUND));

        // 동네 설정
        user.setRegion(regionDTO.getRegion());
        userRepository.save(user);
    }

    @Override
    public Object getAnnouncements() {
        // 공지사항 조회 로직 (필요시 구현)
        return null;
    }
}
