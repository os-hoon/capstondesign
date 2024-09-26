package cherrysumer.cherrysumer.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    private String content;

    private String category;

    @Column(name = "detailed_category", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> detailed_category;

    private String region;


}
