package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.Inventory;
import cherrysumer.cherrysumer.domain.User;
import cherrysumer.cherrysumer.exception.BaseException;
import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.repository.InventoryRepository;
import cherrysumer.cherrysumer.repository.UserRepository;
import cherrysumer.cherrysumer.web.dto.InventoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    @Override
    public List<Inventory> findInventoryByUserId(Long userId) {
        return inventoryRepository.findAllByUserId(userId);
    }

    @Override
    public List<Inventory> findInventoryByStockLocationAndUserId(String stockLocation, Long userId) {
        return inventoryRepository.findByStockLocationAndUserId(stockLocation, userId);
    }

    @Override
    public void insertInventory(Long userId, InventoryDTO inventoryDTO) {
        // 사용자 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode._USER_NOT_FOUND));

        // 새로운 재고 생성
        Inventory inventory = new Inventory();
        inventory.setUser(user);  // User 객체 설정
        inventory.setProductName(inventoryDTO.getProductName());
        inventory.setExpiration_date(inventoryDTO.getExpiration_date());
        inventory.setQuantity(inventoryDTO.getQuantity());
        inventory.setStockLocation(inventoryDTO.getStock_location());
        inventory.setCategory(inventoryDTO.getCategory());

        inventoryRepository.save(inventory);
    }

    @Override
    public void editInventory(Long id, InventoryDTO inventoryDTO) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode._INVENTORY_NOT_FOUND));

        inventory.setProductName(inventoryDTO.getProductName());
        inventory.setExpiration_date(inventoryDTO.getExpiration_date());
        inventory.setQuantity(inventoryDTO.getQuantity());
        inventory.setStockLocation(inventoryDTO.getStock_location());
        inventory.setCategory(inventoryDTO.getCategory());

        inventoryRepository.save(inventory);
    }

    @Override
    public void deleteInventory(Long id) {
        // 1. Inventory 찾기
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode._INVENTORY_NOT_FOUND));


        // 2. Inventory 삭제
        inventoryRepository.delete(inventory);
    }

    @Override
    public List<Inventory> searchInventory(String query) {
        return inventoryRepository.findByProductNameContaining(query);
    }


}
