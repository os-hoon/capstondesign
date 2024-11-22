package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.User;
import cherrysumer.cherrysumer.exception.BaseException;
import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.repository.UserRepository;
import cherrysumer.cherrysumer.util.jwt.TokenProvider;
import cherrysumer.cherrysumer.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ImageUploadService imageUploadService;

    @Override
    public ProfileDTO getProfile() {
        // 사용자 찾기
        User user = userService.getLoggedInUser();

        // 프로필 정보 반환: /image/view/{fileName} 형식으로 프로필 이미지 URL 제공
        String imageUrl = user.getProfileImageUrl() != null ? "/image/view/" + user.getProfileImageUrl() : null;

        // 프로필 정보 반환
        return new ProfileDTO(user.getName(), user.getNickname(), user.getEmail(), user.getRegion(),imageUrl);
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

            // 수정된 정보로 ProfileDTO 생성하여 반환
            return new ProfileDTO(user.getName(), user.getNickname(), user.getEmail(), user.getRegion(), user.getProfileImageUrl());


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


}
