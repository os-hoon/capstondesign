package cherrysumer.cherrysumer.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

public class UserResponseDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class successLoginDTO {
        String token;
        String region;
        String name;
        public successLoginDTO(String region, String name) {
            this.region = region;
            this.name = name;
        }
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class userLoginIdDTO {
        String loginId;
    }
}
