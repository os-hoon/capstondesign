package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.User;
import cherrysumer.cherrysumer.web.dto.MailRequestDTO;
import cherrysumer.cherrysumer.web.dto.UserRequestDTO;
import cherrysumer.cherrysumer.web.dto.UserResponseDTO;
import jakarta.mail.MessagingException;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface UserService {

    UserResponseDTO.successLoginDTO userJoin(UserRequestDTO.userJoinRequestDTO request) throws ParseException;

    void findUserId(String loginId);

    void findNickname(String nickname);

    UserResponseDTO.successLoginDTO userLogin(UserRequestDTO.userLoginRequestDTO request);

    User getLoggedInUser();

    UserResponseDTO.userLoginIdDTO findLoginId(String email);

    void findUserPw(UserRequestDTO.findPwDTO request);

    void changePw(UserRequestDTO.changePwDTO request);

    Point convertPoint(String longitude, String latitude) throws ParseException;
}
