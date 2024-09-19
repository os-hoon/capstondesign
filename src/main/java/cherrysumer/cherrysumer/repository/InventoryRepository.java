package cherrysumer.cherrysumer.repository;

import cherrysumer.cherrysumer.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findAllByUserId(Long userId);  // 특정 사용자의 재고 목록 조회
    List<Inventory> findByProductNameContaining(String query);  // 검색 기능을 위한 메서드

}