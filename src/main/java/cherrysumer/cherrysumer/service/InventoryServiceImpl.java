package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.Category;
import cherrysumer.cherrysumer.domain.Inventory;
import cherrysumer.cherrysumer.domain.User;
import cherrysumer.cherrysumer.exception.BaseException;
import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.repository.CategoryRepository;
import cherrysumer.cherrysumer.repository.InventoryRepository;
import cherrysumer.cherrysumer.repository.UserRepository;
import cherrysumer.cherrysumer.web.dto.InventoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    @Override
    public List<Inventory> findInventoryByUserId(Long userId) {
        return inventoryRepository.findAllByUserId(userId);
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
        inventory.setExpiration_date(inventoryDTO.getExpirationDate());
        inventory.setQuantity(inventoryDTO.getQuantity());
        inventory.setStock_location(inventoryDTO.getStockLocation());
        inventory.setCategory(inventoryDTO.getCategory());
        inventory.setDetailed_category(inventoryDTO.getDetailedCategory());

        inventoryRepository.save(inventory);
    }

    @Override
    public void editInventory(Long id, InventoryDTO inventoryDTO) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode._INVENTORY_NOT_FOUND));

        inventory.setProductName(inventoryDTO.getProductName());
        inventory.setExpiration_date(inventoryDTO.getExpirationDate());
        inventory.setQuantity(inventoryDTO.getQuantity());
        inventory.setStock_location(inventoryDTO.getStockLocation());
        inventory.setCategory(inventoryDTO.getCategory());
        inventory.setDetailed_category(inventoryDTO.getDetailedCategory());

        inventoryRepository.save(inventory);
    }

    @Override
    public void deleteInventory(Long id) {
        // 1. Inventory 찾기
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode._INVENTORY_NOT_FOUND));

        // 2. Inventory에 연결된 카테고리 ID들 가져오기
        if (inventory.getCategory() != null){
            List<Long> categoryIds = inventory.getCategory();

            // 3. 각 카테고리를 삭제
            categoryIds.forEach(categoryId -> {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new BaseException(ErrorCode._CATEGORY_NOT_FOUND));
                categoryRepository.delete(category);
            });

        }

        // 4. Inventory 삭제
        inventoryRepository.delete(inventory);
    }

    @Override
    public List<Inventory> searchInventory(String query) {
        return inventoryRepository.findByProductNameContaining(query);
    }

    @Override
    public void addCategoryToInventory(Long inventoryId, String categoryName) {
        // 1. 해당 Inventory 찾기
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new BaseException(ErrorCode._INVENTORY_NOT_FOUND));

        // 2. Inventory의 카테고리 리스트가 null인지 확인하고 초기화
        if (inventory.getCategory() == null) {
            inventory.setCategory(new ArrayList<>());  // null일 경우 빈 리스트로 초기화
        }

        // 3. Inventory 내에서 해당 카테고리 이름이 이미 존재하는지 확인
        boolean isDuplicate = inventory.getCategory().stream()
                .anyMatch(categoryId -> categoryRepository.findById(categoryId)
                        .map(cat -> cat.getName().equals(categoryName)).orElse(false));

        // 4. 중복된 카테고리가 없으면 카테고리 생성 또는 가져오기
        if (!isDuplicate) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseGet(() -> categoryService.createCategory(categoryName));

            // 5. Inventory의 카테고리 리스트에 새로 생성된 카테고리 추가
            inventory.getCategory().add(category.getId());
            inventoryRepository.save(inventory);
        } else {
            throw new BaseException(ErrorCode._CATEGORY_DUPLICATE);
        }
    }



}
