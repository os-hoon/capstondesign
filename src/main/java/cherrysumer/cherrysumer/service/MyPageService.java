package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.web.dto.ProfileDTO;
import cherrysumer.cherrysumer.web.dto.RegionDTO;

public interface MyPageService {

    ProfileDTO getProfile(Long userId);

    void modifyProfile(Long userId, ProfileDTO profileDTO);

    void setRegion(Long userId, RegionDTO regionDTO);

    Object getAnnouncements();
}
