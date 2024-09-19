package cherrysumer.cherrysumer.repository;

import cherrysumer.cherrysumer.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // 카테고리 이름으로 카테고리 찾기
    Optional<Category> findByName(String name);  // name으로 카테고리 검색

    // 카테고리 이름이 이미 존재하는지 확인하는 메서드
    boolean existsByName(String name);
}
