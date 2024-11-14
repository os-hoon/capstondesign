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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public List<InventoryDTO> findFilteredInventory(String category, String filter) {
        User user = userService.getLoggedInUser();
        List<Inventory> inventories;

        if (category == null) {
            // 카테고리가 선택되지 않았을 경우 모든 카테고리 조회
            inventories = inventoryRepository.findAllByUserIdOrderByUpdatedAtDesc(user.getId());
        } else {
            // 특정 카테고리 필터링
            inventories = inventoryRepository.findAllByUserIdAndCategoryOrderByUpdatedAtDesc(user.getId(), category);
        }

        switch (filter) {
            case "최신순":
                // 기본적으로 최신순이므로 이미 정렬되어 있음
                break;
            case "유통기한 임박순":
                inventories.sort(Comparator.comparing(Inventory::getExpiration_date));
                break;
            case "수량 적은 순":
                inventories.sort(Comparator.comparingInt(Inventory::getQuantity));
                break;
            case "수량 많은 순":
                inventories.sort((o1, o2) -> o2.getQuantity() - o1.getQuantity());
                break;
            default:
                throw new BaseException(ErrorCode._INVENTORY_INVALID_FILTER);
        }

        return inventories.stream()
                .map(this::convertToInventoryDTO)
                .collect(Collectors.toList());
    }

    // 새로운 category, filter, stockLocation 필터링 로직
    @Override
    public List<InventoryDTO> findFilteredInventoryWithStockLocation(String category, String filter, String stockLocation) {
        User user = userService.getLoggedInUser();
        List<Inventory> inventories;

        if (category == null && stockLocation == null) {
            inventories = inventoryRepository.findAllByUserIdOrderByUpdatedAtDesc(user.getId());
        } else if (category == null) {
            inventories = inventoryRepository.findAllByUserIdAndStockLocationOrderByUpdatedAtDesc(user.getId(), stockLocation);
        } else if (stockLocation == null) {
            inventories = inventoryRepository.findAllByUserIdAndCategoryOrderByUpdatedAtDesc(user.getId(), category);
        } else {
            inventories = inventoryRepository.findAllByUserIdAndCategoryAndStockLocationOrderByUpdatedAtDesc(user.getId(), category, stockLocation);
        }

        // 정렬 필터 적용
        switch (filter) {
            case "최신순":
                break;
            case "유통기한 임박순":
                inventories.sort(Comparator.comparing(Inventory::getExpiration_date));
                break;
            case "수량 적은 순":
                inventories.sort(Comparator.comparingInt(Inventory::getQuantity));
                break;
            case "수량 많은 순":
                inventories.sort((o1, o2) -> o2.getQuantity() - o1.getQuantity());
                break;
            default:
                throw new BaseException(ErrorCode._INVENTORY_INVALID_FILTER);
        }

        return inventories.stream()
                .map(this::convertToInventoryDTO)
                .collect(Collectors.toList());
    }

    private InventoryDTO convertToInventoryDTO(Inventory inventory) {
        return new InventoryDTO(
                inventory.getProductName(),
                inventory.getPurchase_date(),
                inventory.getExpiration_date(),
                inventory.getQuantity(),
                inventory.getStockLocation(),
                inventory.getCategory()
        );
    }

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
        inventory.setPurchase_date(inventoryDTO.getPurchase_date());
        inventory.setExpiration_date(inventoryDTO.getExpiration_date());
        inventory.setQuantity(inventoryDTO.getQuantity());
        inventory.setStockLocation(inventoryDTO.getStockLocation());
        inventory.setCategory(inventoryDTO.getCategory());

        inventoryRepository.save(inventory);
    }

    @Override
    public void editInventory(Long id, InventoryDTO inventoryDTO) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode._INVENTORY_NOT_FOUND));

        inventory.setProductName(inventoryDTO.getProductName());
        inventory.setPurchase_date(inventoryDTO.getPurchase_date());
        inventory.setExpiration_date(inventoryDTO.getExpiration_date());
        inventory.setQuantity(inventoryDTO.getQuantity());
        inventory.setStockLocation(inventoryDTO.getStockLocation());
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
    public List<Inventory> searchInventory(String query, Long id) {
        return inventoryRepository.findByProductNameContainingAndUserId(query, id);
    }



}