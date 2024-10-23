package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.web.dto.*;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.springframework.web.multipart.MultipartFile;

public interface MyPageService {

    ProfileDTO getProfile(Long userId);

    void modifyProfile(Long userId, ProfileDTO profileDTO,MultipartFile file);

    RegionResponseDTO.successregionDTO setRegion(Long userId, RegionDTO request) throws ParseException;

    Point convertPoint(String longitude, String latitude) throws ParseException;

}
