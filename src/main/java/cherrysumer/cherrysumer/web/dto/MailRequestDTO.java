package cherrysumer.cherrysumer.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MailRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class verificationRequestDTO {
        @NotEmpty(message = "이메일을 입력하세요.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
        String email;

        @NotEmpty(message = "인증 번호를 입력하세요.")
        @Pattern(regexp = "^\\d{6}$", message = "인증 번호가 올바르지 않습니다. 6자리 숫자를 입력하세요.")
        String code;
    }
}
