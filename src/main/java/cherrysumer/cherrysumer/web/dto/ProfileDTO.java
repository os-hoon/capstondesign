package cherrysumer.cherrysumer.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {
    @NotEmpty(message = "이름을 입력하세요.")
    private String name;

    @NotEmpty(message = "닉네임을 입력하세요.")
    private String nickname;

    @NotEmpty(message = "이메일을 입력하세요.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    private String region;

    private String profileImageUrl;
    // 내부 클래스 (로그인 아이디 포함)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Extended {
        private String name;
        private String nickname;
        private String email;
        private String region;
        private String profileImageUrl;
        private String loginId; // 로그인 아이디 추가
    }
}
