package cherrysumer.cherrysumer.web.controller;

import cherrysumer.cherrysumer.service.MailService;
import cherrysumer.cherrysumer.service.UserService;
import cherrysumer.cherrysumer.web.dto.MailRequestDTO;
import cherrysumer.cherrysumer.web.dto.UserRequestDTO;
import cherrysumer.cherrysumer.web.dto.UserResponseDTO;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final MailService mailService;

    @PostMapping("/join")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDTO.userJoinRequestDTO request) {
        userService.userJoin(request);
        return ResponseEntity.status(HttpStatus.OK).body("회원가입이 성공하였습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO.successLoginDTO> loginUser(@Valid @RequestBody UserRequestDTO.userLoginRequestDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.userLogin(request));
    }

    // 아이디 중복 확인
    @GetMapping("/id-exists")
    public ResponseEntity<?> checkExistId(@RequestParam("loginId") String loginId) {
        userService.findUserId(loginId);
        return ResponseEntity.status(HttpStatus.OK).body("사용가능한 아이디입니다.");
    }

    // 닉네임 중복 확인
    @GetMapping("/nickname-exists")
    public ResponseEntity<?> checkExistNickname(@RequestParam("nickname") String nickname) {
        userService.findNickname(nickname);
        return ResponseEntity.status(HttpStatus.OK).body("사용가능한 닉네임입니다.");
    }

    // 이메일 인증 코드 요청
    @GetMapping("/email-verification")
    public ResponseEntity<?> requestverificationCode(@RequestParam("email") String email) throws NoSuchAlgorithmException, MessagingException {
        mailService.sendCode(email);
        return ResponseEntity.status(HttpStatus.OK).body("인증 번호 전송 완료");
    }

    // 이메일 인증
    @PostMapping("/email-verification")
    public ResponseEntity<?> verificationCode(@RequestBody MailRequestDTO.verificationRequestDTO request) throws NoSuchAlgorithmException {
        mailService.checkCode(request);
        return ResponseEntity.status(HttpStatus.OK).body("이메일 인증 성공");
    }

    // jwt 인증 테스트
    @GetMapping("/jwt-test")
    public ResponseEntity<?> jwtTest() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getLoggedInUser().getNickname());
    }
}
