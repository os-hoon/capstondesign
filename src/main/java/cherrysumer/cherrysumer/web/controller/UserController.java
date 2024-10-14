package cherrysumer.cherrysumer.web.controller;

import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.exception.handler.PostErrorHandler;
import cherrysumer.cherrysumer.exception.handler.UserErrorHandler;
import cherrysumer.cherrysumer.service.FileUploadService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import cherrysumer.cherrysumer.domain.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final MailService mailService;
    private final FileUploadService fileUploadService;

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

    // 로그인 여부 확인
    @GetMapping("/auth")
    public ApiResponse<?> test() {
        return ApiResponse.onSuccess(userService.loginAuth());
    }

    // 프로필 이미지 업로드 API
    @PutMapping("/profile-image")
    public ApiResponse<?> uploadProfileImage(@RequestPart("file") MultipartFile file,@AuthenticationPrincipal User authenticatedUser) {
        try {
            // 파일 업로드 처리
            String filePath = fileUploadService.uploadProfileImage(file);

            // 이미지 경로 DTO 생성
            UserRequestDTO.updateProfileImageDTO updateProfileImageDTO = new UserRequestDTO.updateProfileImageDTO(filePath);

            // 유저의 프로필 이미지 경로 업데이트
            userService.updateProfileImage(authenticatedUser, updateProfileImageDTO);

            return ApiResponse.onSuccess("프로필 이미지가 성공적으로 업로드되었습니다.");
        } catch (IOException e) {
            return ApiResponse.onFailure(ErrorCode._IMAGE_NOT_FOUND, "업로드 실패");
        }
    }

    // 프로필 이미지 조회 API
    @GetMapping("/profile-image")
    public ApiResponse<?> getUserProfileImage(@RequestBody UserRequestDTO.updateProfileImageDTO request) {
        User user = userService.getLoggedInUser();  // 여기서는 실제 인증된 사용자 정보 사용
        String profileImageUrl = user.getProfileImageUrl();

        return ApiResponse.onSuccess(profileImageUrl);

    }

}
