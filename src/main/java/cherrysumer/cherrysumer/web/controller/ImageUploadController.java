package cherrysumer.cherrysumer.web.controller;
import cherrysumer.cherrysumer.domain.User;
import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.repository.UserRepository;
import cherrysumer.cherrysumer.service.ImageUploadService;
import cherrysumer.cherrysumer.service.UserService;
import cherrysumer.cherrysumer.util.ApiResponse;
import cherrysumer.cherrysumer.web.dto.ImageUploadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageUploadController {

    private final ImageUploadService imageUploadService;
    private final UserRepository userRepository;
    private final UserService userService;



    // 이미지 조회 API
    @GetMapping("/view/{fileName}")
    public ApiResponse<?> viewImage(@PathVariable String fileName) {
        try {
            // 파일 경로
            Path filePath = Paths.get("/home/ubuntu/images/" + fileName);

            // 이미지 파일을 Resource로 읽기
            Resource resource = new UrlResource(filePath.toUri());

            // 파일이 존재하는지 확인
            if (!resource.exists()) {
                return ApiResponse.onFailure(ErrorCode._IMAGE_NOT_FOUND, "이미지를 찾을 수 없습니다.");
            }

            // 이미지 파일 URL을 반환
            String imageUrl = "/home/ubuntu/images/" + fileName;

            return ApiResponse.onSuccess(new ImageUploadDTO(imageUrl));

        } catch (MalformedURLException e) {
            return ApiResponse.onFailure(ErrorCode._IMAGE_NOT_FOUND, "잘못된 파일 경로입니다.");
        }
    }
}