package cherrysumer.cherrysumer.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Post extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String productname;

    // 모집 인원
    private int capacity;

    private LocalDateTime date;

    private int price;

    private String place;

    // 좌표
    @Column(columnDefinition = "GEOMETRY")
    private Point point;

    private String content;

    @Column(name = "category", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> category;

    @Column(name = "detailed_category", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> detailed_category;

    // 동네
    private String region;
    private String regionCode;

    private boolean isClosed; // 마감 여부

    // 재고 등록 여부
    //private boolean isRegist;

    private boolean isCompleted; // 거래 완료 여부

    @Column(name = "postImage", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Image> postImage;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Image {
        @JsonProperty("originfilename")
        private String originfilename;

        @JsonProperty("imagepath")
        private String imagepath;
    }

}

