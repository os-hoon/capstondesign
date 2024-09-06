package cherrysumer.cherrysumer.repository;

import cherrysumer.cherrysumer.domain.MailCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailCodeRepository extends JpaRepository<MailCode, Long> {

    MailCode findByEmail(String email);

    boolean existsByEmail(String email);
}
