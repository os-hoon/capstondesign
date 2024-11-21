package cherrysumer.cherrysumer.repository;

import cherrysumer.cherrysumer.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findAllByUserId(Long userId);  // 특정 사용자의 재고 목록 조회
    List<Inventory> findByProductNameContainingAndUserId(String query, Long userId);  // 검색 기능을 위한 메서드

    List<Inventory> findByStockLocationAndUserId(String stockLocation, Long userId);

    List<Inventory> findAllByUserIdOrderByUpdatedAtDesc(Long userId);
    List<Inventory> findAllByUserIdAndCategoryOrderByUpdatedAtDesc(Long userId, String category);
    List<Inventory> findAllByUserIdAndStockLocationOrderByUpdatedAtDesc(Long userId, String stockLocation);
    List<Inventory> findAllByUserIdAndCategoryAndStockLocationOrderByUpdatedAtDesc(Long userId, String category, String stockLocation);

    @Query("SELECT i.productName FROM Inventory i WHERE i.category = '생활용품' AND i.quantity <= 2 AND i.user.id = :userId")
    List<String> findLowQuantityItems(@Param("userId") Long userId);

    @Query("SELECT i.productName FROM Inventory i WHERE i.category != '생활용품' AND i.expiration_date <= :date AND i.user.id = :userId")
    List<String> findNearExpirationItems(@Param("userId") Long userId, @Param("date") LocalDateTime date);

}
