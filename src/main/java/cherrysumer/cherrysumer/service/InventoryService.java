package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.Inventory;
import cherrysumer.cherrysumer.web.dto.InventoryDTO;

import java.util.List;

public interface InventoryService {
    List<Inventory> findInventoryByUserId(Long userId);
    void insertInventory(Long userId, InventoryDTO inventoryDTO);
    void editInventory(Long id, InventoryDTO inventoryDTO);
    void deleteInventory(Long id);
    List<Inventory> searchInventory(String query);
    void addCategoryToInventory(Long inventoryId, String categoryName);
}
