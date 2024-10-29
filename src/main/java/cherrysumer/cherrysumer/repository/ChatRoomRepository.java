package cherrysumer.cherrysumer.repository;

import cherrysumer.cherrysumer.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    // 기본적인 CRUD 메서드는 JpaRepository에서 제공됩니다.
    // 필요한 경우 여기에 추가적인 쿼리 메서드를 정의할 수 있습니다.
}
