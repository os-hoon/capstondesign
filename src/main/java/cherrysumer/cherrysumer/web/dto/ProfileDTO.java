package cherrysumer.cherrysumer.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {
    private String name;
    private String nickname;
    private String email;
    private String region;  // 현재 거주 지역
}
