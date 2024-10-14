package cherrysumer.cherrysumer.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    String uploadProfileImage(MultipartFile file) throws IOException;
}