package cherrysumer.cherrysumer.web.controller;

import cherrysumer.cherrysumer.domain.User;
import cherrysumer.cherrysumer.service.MailService;
import cherrysumer.cherrysumer.service.UserService;
import cherrysumer.cherrysumer.util.ApiResponse;
import cherrysumer.cherrysumer.web.dto.MailRequestDTO;
import cherrysumer.cherrysumer.web.dto.UserRequestDTO;
import cherrysumer.cherrysumer.web.dto.UserResponseDTO;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
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
    public ApiResponse<?> createUser(@Valid @RequestBody UserRequestDTO.userJoinRequestDTO request) throws ParseException {
        //userService.userJoin(request);
        return ApiResponse.onSuccess(userService.userJoin(request));
    }

    @PostMapping("/login")
    public ApiResponse<UserResponseDTO.successLoginDTO> loginUser(@Valid @RequestBody UserRequestDTO.userLoginRequestDTO request) {
        return ApiResponse.onSuccess(userService.userLogin(request));
    }

    // 아이디 중복 확인
    @GetMapping("/id-exists")
    public ApiResponse<?> checkExistId(@RequestParam("loginId") String loginId) {
        userService.findUserId(loginId);
        return ApiResponse.onSuccess("사용가능한 아이디입니다.");
    }

    // 닉네임 중복 확인
    @GetMapping("/nickname-exists")
    public ApiResponse<?> checkExistNickname(@RequestParam("nickname") String nickname) {
        userService.findNickname(nickname);
        return ApiResponse.onSuccess("사용가능한 닉네임입니다.");
    }

    // 이메일 인증 코드 요청
    @GetMapping("/email-verification")
    public ApiResponse<?> requestverificationCode(@RequestParam("email") String email) throws NoSuchAlgorithmException, MessagingException {
        mailService.sendCode(email);
        return ApiResponse.onSuccess("인증 번호 전송 완료");
    }

    // 이메일 인증
    @PostMapping("/email-verification")
    public ApiResponse<?> verificationCode(@Valid @RequestBody MailRequestDTO.verificationRequestDTO request) throws NoSuchAlgorithmException {
        mailService.checkCode(request);
        return ApiResponse.onSuccess("이메일 인증 성공");
    }

    // 아이디 찾기
    @GetMapping("/findId")
    public ApiResponse<?> findId(@RequestParam("email") String email) {
        return ApiResponse.onSuccess(userService.findLoginId(email));
    }

    // 비밀번호 찾기
    @PostMapping("/findPwd")
    public ApiResponse<?> findPw(@Valid @RequestBody UserRequestDTO.findPwDTO request) {
        userService.findUserPw(request);
        return ApiResponse.onSuccess("인증이 완료되었습니다.");
    }
    // 비밀번호 찾기 -> 비밀번호 변경
    @PostMapping("/changePwd")
    public ApiResponse<?> changePw(@Valid @RequestBody UserRequestDTO.changePwDTO request) {
        userService.changePw(request);
        return ApiResponse.onSuccess("비밀번호가 변경되었습니다.");
    }
}
