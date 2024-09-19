package cherrysumer.cherrysumer.web.dto;

import cherrysumer.cherrysumer.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PostResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class postDTO {
        private Long id;
        private String title;
        private String place;
        private LocalDateTime created_at;
        private String upload;
        private int price;

        private int likes;
        private boolean like_status;

        public postDTO(Post p, int likes, boolean like_status, String upload) {
            this.id = p.getId();
            this.title = p.getTitle();
            this.place = p.getPlace();
            this.price = p.getPrice();
            this.created_at = p.getCreatedAt();

            this.upload = upload;
            this.likes = likes;
            this.like_status = like_status;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class successPostDTO {
        private Long id;
        private String title;
        private int price;
        private String place;
        private LocalDateTime date;

        public successPostDTO(Post p) {
            this.id = p.getId();
            this.title = p.getTitle();
            this.place = p.getPlace();
            this.price = p.getPrice();
            this.date = p.getDate();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class recruitDTO {
        private String title;
        private int number;
        private Long postId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class applicationDTO {
        private String title;
        private Long postId;
        private int isConfirmed;
    }
}
