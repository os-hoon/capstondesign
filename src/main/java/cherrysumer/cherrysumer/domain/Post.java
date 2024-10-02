package cherrysumer.cherrysumer.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
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

    // 모집 인원
    private int capacity;

    private LocalDateTime date;

    private int price;

    private String place;

    // 좌표
    @Column(columnDefinition = "GEOMETRY")
    private Point point;

    private String content;

    private String category;

    @Column(name = "detailed_category", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> detailed_category;

    // 동네
    private String regionCode;

    private boolean isClosed;

}
