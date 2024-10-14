package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.web.dto.ProfileDTO;
import cherrysumer.cherrysumer.web.dto.RegionDTO;
import org.springframework.web.multipart.MultipartFile;

public interface MyPageService {

    ProfileDTO getProfile(Long userId);

    void modifyProfile(Long userId, ProfileDTO profileDTO,MultipartFile file);

    void setRegion(Long userId, RegionDTO regionDTO);

    Object getAnnouncements();
}
