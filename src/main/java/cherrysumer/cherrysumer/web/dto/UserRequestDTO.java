package cherrysumer.cherrysumer.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

public class UserRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class userJoinRequestDTO {
        @NotEmpty(message = "아이디를 입력하세요.")
        private String loginId;

        @NotEmpty(message = "비밀번호를 입력하세요.")
        private String passwd;

        @NotEmpty(message = "이름을 입력하세요.")
        private String name;

        @NotEmpty(message = "닉네임을 입력하세요.")
        private String nickname;

        @NotEmpty(message = "이메일을 입력하세요.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
        private String email;

        private List<Long> category;

        private String region;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class existUserIdRequestDTO {
        @NotEmpty(message = "아이디를 입력하세요.")
        private String loginId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class existUserNicknameRequestDTO {
        @NotEmpty(message = "닉네임을 입력하세요.")
        private String nickname;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class userLoginRequestDTO {
        @NotEmpty(message = "아이디, 비밀번호를 입력하세요.")
        private String loginId;
        @NotEmpty(message = "아이디, 비밀번호를 입력하세요.")
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class userInfoDTO {
        private Long id;
    }
}
