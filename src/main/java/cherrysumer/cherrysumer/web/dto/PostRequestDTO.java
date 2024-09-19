package cherrysumer.cherrysumer.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class PostRequestDTO {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class addPostDTO {
        @NotEmpty(message = "제목을 입력하세요.")
        private String title;
        @NotEmpty(message = "모집 인원을 입력하세요.")
        private int capacity;
        @NotEmpty(message = "날짜, 시간을 입력하세요.")
        private LocalDateTime date;
        @NotEmpty(message = "가격을 입력하세요")
        private int price;
        @NotEmpty(message = "장소를 입력하세요")
        private String place;

        private String content;

        private String category;

        List<String> detailed_category;
    }
}
