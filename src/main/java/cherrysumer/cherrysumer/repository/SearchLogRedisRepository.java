package cherrysumer.cherrysumer.repository;

import cherrysumer.cherrysumer.domain.SearchLogRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SearchLogRedisRepository extends CrudRepository<SearchLogRedis, String> {
    Optional<SearchLogRedis> findByNameAndCreatedAtAndState(String name, LocalDateTime createdAt, Boolean state);
}
