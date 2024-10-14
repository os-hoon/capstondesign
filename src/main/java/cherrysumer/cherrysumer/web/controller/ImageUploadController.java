package cherrysumer.cherrysumer.web.controller;
import cherrysumer.cherrysumer.domain.User;
import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.repository.UserRepository;
import cherrysumer.cherrysumer.service.ImageUploadService;
import cherrysumer.cherrysumer.util.ApiResponse;
import cherrysumer.cherrysumer.web.dto.ImageUploadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageUploadController {

    private final ImageUploadService imageUploadService;
    private final UserRepository userRepository;

    // 이미지 업로드 및 사용자 프로필 이미지 업데이트 API
    @PostMapping("/upload")
    public ApiResponse<?> uploadImage(@RequestPart("file") MultipartFile file) {
        

        try {
            // 파일 업로드 처리
            String filePath = imageUploadService.uploadImage(file);

            // 인증된 사용자 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User authenticatedUser = (User) authentication.getPrincipal();

            // 프로필 이미지 경로 업데이트
            authenticatedUser.setProfileImageUrl(filePath);
            userRepository.save(authenticatedUser);

            // 업로드된 이미지 경로 반환
            ImageUploadDTO imageUploadDTO = new ImageUploadDTO(filePath);

            return ApiResponse.onSuccess(imageUploadDTO);
        } catch (IOException e) {
            return ApiResponse.onFailure(ErrorCode._IMAGE_NOT_FOUND,"업로드 실패");
        }
    }
}