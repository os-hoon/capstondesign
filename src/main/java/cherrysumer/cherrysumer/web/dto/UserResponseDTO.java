package cherrysumer.cherrysumer.web.dto;

import lombok.*;

public class UserResponseDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class successLoginDTO {
        String token;
    }
}
