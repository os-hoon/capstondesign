package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.Inventory;
import cherrysumer.cherrysumer.web.dto.InventoryDTO;

import java.util.List;

public interface InventoryService {
    List<Inventory> findInventoryByUserId();
    List<Inventory> findInventoryByStockLocationAndUserId(String stockLocation);
    void insertInventory(InventoryDTO inventoryDTO);
    void editInventory(Long id, InventoryDTO inventoryDTO);
    void deleteInventory(Long id);
    List<Inventory> searchInventory(String query);
    List<InventoryDTO> findFilteredInventory(String categories, String filter);
    // 새로운 category, filter, stockLocation 필터링
    List<InventoryDTO> findFilteredInventoryWithStockLocation(String category, String filter, String stockLocation);

    List<String> getFilteredProductNames(); // 조건에 맞는 품목의 상품명을 반환
}
