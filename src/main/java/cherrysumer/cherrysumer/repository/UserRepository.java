package cherrysumer.cherrysumer.repository;

import cherrysumer.cherrysumer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsUserByLoginId(String loginId);
    Boolean existsUserByNickname(String nickname);

    User findUserByLoginId(String loginId);
    User findUserByNickname(String nickname);
    User findUserById(Long userId);
}
