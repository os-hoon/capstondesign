package cherrysumer.cherrysumer.web.dto;

import cherrysumer.cherrysumer.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class postDTO {
        private Long postId;
        private String title;
        private String productname;
        private String region;
        //private LocalDateTime created_at;
        private String upload;
        private int price;

        private int likes;
        private boolean like_status;

        private boolean isClosed;

        //private String longitude; // 경도
        //private String latitude; // 위도

        public postDTO(Post p, int likes, boolean like_status, String upload) {
            this.postId = p.getId();
            this.title = p.getTitle();
            this.productname = p.getProductname();
            this.region = p.getUser().getRegion();
            this.price = (int) Math.round(p.getPrice() / (double) p.getCapacity());
            //this.created_at = p.getCreatedAt();

            this.upload = upload;
            this.likes = likes;
            this.like_status = like_status;
            this.isClosed = p.isClosed();

            //this.longitude = Double.toString(p.getPoint().getY());
            //this.latitude = Double.toString(p.getPoint().getX());

        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class summaryPostDTO {
        private Long postId;
        private String title;
        private String productname;
        private int price;
        private String place;
        private LocalDateTime date;

        public summaryPostDTO(Post p) {
            this.postId = p.getId();
            this.title = p.getTitle();
            this.productname = p.getProductname();
            this.place = p.getPlace();
            this.price = Math.round(p.getPrice() / p.getCapacity());
            this.date = p.getDate();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class recruitDTO {
        private Long postId;
        private String title;
        private String productname;
        private int number;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class applicationDTO {
        private Long postId;
        private String title;
        private String productname;
        private String isConfirmed;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class likePostDTO {
        private Long postId;
        private boolean like_status;
        private int likes;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class closePostDTO {
        private Long postId;
        private boolean isClosed;

        public closePostDTO(Post p) {
            this.postId = p.getId();
            this.isClosed = p.isClosed();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class participateUserDTO {
        private Long userId;
        private Long postId;
        private String nickname;
        private String region;
        private String isConfirmed;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class detailPostDTO {
        /*
        게시글 id, 작성자 닉네임, 세부 카테고리, 제목, 동네, 업로드 시간,
        - 인당 가격, 날짜/시간, 장소, 모집 인원, 내용
        - 좋아요 수, 여부, 참여 버튼 활성화 여부, 내가 쓴 글인지 여부, 마감 여부
         */
        private Long postId;
        private String writer;
        private List<String> detail_category;
        private List<String> category;
        private String title;
        private String productname;
        private String upload;
        private int price;
        private LocalDateTime date;
        private int capacity;
        private String content;
        private String place;

        private int likes;
        private boolean like_status;

        private boolean isClosed; // 마감 여부
        private boolean isAuthor; // 작성 여부
        private boolean isJoin; // 참여 가능 여부

        public detailPostDTO(Post p, String upload, int likes, boolean like_status, boolean isAuthor, boolean isJoin) {
            this.postId = p.getId();
            this.writer = p.getUser().getNickname();
            this.detail_category = p.getDetailed_category();
            this.category = p.getCategory();
            this.title = p.getTitle();
            this.productname = p.getProductname();
            this.price = (int) Math.round(p.getPrice() / (double) p.getCapacity());
            this.date = p.getDate();
            this.capacity = p.getCapacity();
            this.content = p.getContent();
            this.place = p.getPlace();

            this.upload = upload;
            this.likes = likes;
            this.like_status = like_status;

            this.isClosed = p.isClosed();
            this.isAuthor = isAuthor;
            this.isJoin = isJoin;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class postDataDTO {
        private Long postId;
        private String title;
        private String productname;
        private String region;
        private int price;

        public postDataDTO(Post p) {
            this.postId = p.getId();
            this.title = p.getTitle();
            this.productname = p.getProductname();
            this.region = p.getUser().getRegion();
            this.price = (int) Math.round(p.getPrice() / (double) p.getCapacity());
        }
    }
}
