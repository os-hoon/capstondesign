package cherrysumer.cherrysumer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@Table(name = "ChatMessage")
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(value = {AuditingEntityListener.class})
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "roomId", insertable = false, updatable = false)
    private String roomId; //단순히 ID 값만 필요  (ChatRoom)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore  // 이 줄을 추가하여 User 정보가 JSON에 포함되지 않도록 설정
    private User user;

    @Column(name = "message")
    private String message;

}
