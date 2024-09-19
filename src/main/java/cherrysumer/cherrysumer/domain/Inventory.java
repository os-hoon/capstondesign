package cherrysumer.cherrysumer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Inventory extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore  // 이 줄을 추가하여 User 정보가 JSON에 포함되지 않도록 설정
    private User user;

    private String productName;

    private LocalDateTime expiration_date;

    private int quantity;

    private String stock_location;

    @Column(name = "category", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Long> category;

    @Column(name = "detailed_category", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> detailed_category;
}
