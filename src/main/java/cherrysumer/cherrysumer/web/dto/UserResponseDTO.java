package cherrysumer.cherrysumer.web.dto;

import lombok.*;

public class UserResponseDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class successLoginDTO {
        String token;
        String region;
        String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class userLoginIdDTO {
        String loginId;
    }
}
