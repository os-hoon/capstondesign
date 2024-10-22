package cherrysumer.cherrysumer.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionDTO {
    private String region;  // 사용자의 현재 거주지

    private String regionCode;

    private String longitude; // 경도

    private String latitude; // 위도
}
