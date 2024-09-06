package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.User;
import cherrysumer.cherrysumer.web.dto.MailRequestDTO;
import cherrysumer.cherrysumer.web.dto.UserRequestDTO;
import cherrysumer.cherrysumer.web.dto.UserResponseDTO;
import jakarta.mail.MessagingException;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface UserService {

    void userJoin(UserRequestDTO.userJoinRequestDTO request);

    void findUserId(UserRequestDTO.existUserIdRequestDTO request);

    void findNickname(UserRequestDTO.existUserNicknameRequestDTO request);

    UserResponseDTO.successLoginDTO userLogin(UserRequestDTO.userLoginRequestDTO request);

    User getLoggedInUser();
}
