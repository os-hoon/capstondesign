package cherrysumer.cherrysumer.repository;

import cherrysumer.cherrysumer.domain.SearchLogRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchLogRedisRepository extends CrudRepository<SearchLogRedis, String> {
    // 기본 CRUD 메소드가 Redis에서 사용될 수 있도록 정의됨
}
