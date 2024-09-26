package cherrysumer.cherrysumer.repository;

import cherrysumer.cherrysumer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsUserByLoginId(String loginId);
    Boolean existsUserByNickname(String nickname);

    Optional<User> findByLoginId(String loginId);
    Optional<User> findByEmail(String email);
    User findByNickname(String nickname);
    //User findById(Long userId);
}
