package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.service.ImageUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {

    private final String uploadDir = "/home/ubuntu/images";  // 이미지가 저장될 경로

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        // 파일명이 중복되지 않도록 UUID로 파일명을 생성
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + extension;

        // 저장 경로 설정
        Path path = Paths.get(uploadDir, newFileName);
        Files.createDirectories(path.getParent());  // 디렉토리가 없으면 생성
        Files.write(path, file.getBytes());  // 파일을 지정된 경로에 저장

        // 절대 경로 리턴
        return path.toString();
    }
}
