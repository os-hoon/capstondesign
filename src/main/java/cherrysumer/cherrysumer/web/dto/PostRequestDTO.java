package cherrysumer.cherrysumer.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class PostRequestDTO {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class postDTO {
        private Long postId; // 수정용
        @NotEmpty(message = "제목을 입력하세요.")
        private String title;
        @NotEmpty(message = "상품명을 입력하세요.")
        private String productname;
        @NotNull(message = "모집 인원을 입력하세요.")
        private int capacity;
        @NotNull(message = "날짜, 시간을 입력하세요.")
        private LocalDateTime date;
        /*@NotEmpty(message = "날짜를 입력하세요.")
        private String date;
        @NotEmpty(message = "시간을 입력하세요.")
        private String time;*/
        @NotNull(message = "가격을 입력하세요")
        private int price;
        @NotEmpty(message = "장소를 입력하세요")
        private String place;

        private String content;

        private List<String> category;

        private List<String> detailed_category;

        private String longitude; // 경도

        private String latitude; // 위도
    }
}
