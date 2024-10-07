package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.Inventory;
import cherrysumer.cherrysumer.web.dto.InventoryDTO;

import java.util.List;

public interface InventoryService {
    List<Inventory> findInventoryByUserId(Long userId);
    List<Inventory> findInventoryByStockLocationAndUserId(String stockLocation, Long userId);
    void insertInventory(Long userId, InventoryDTO inventoryDTO);
    void editInventory(Long id, InventoryDTO inventoryDTO);
    void deleteInventory(Long id);
    List<Inventory> searchInventory(String query);
    List<InventoryDTO> findFilteredInventory(List<String> categories, String filter);
}
