package cherrysumer.cherrysumer.web.dto;

import jakarta.validation.constraints.NotEmpty;
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
    private String email;

    private String region;  // 현재 거주 지역
}
