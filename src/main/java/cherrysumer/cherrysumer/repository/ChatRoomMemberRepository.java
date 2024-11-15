package cherrysumer.cherrysumer.repository;

import cherrysumer.cherrysumer.domain.ChatRoomMember;
import cherrysumer.cherrysumer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    // 로그인된 사용자와 연관된 ChatRoomMember 조회
    List<ChatRoomMember> findByUser(User user);

    // 특정 chatRoomId로 ChatRoomMember 조회
    List<ChatRoomMember> findByChatRoomId(String chatRoomId);
}
