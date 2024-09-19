package cherrysumer.cherrysumer.web.controller;

import cherrysumer.cherrysumer.domain.Inventory;
import cherrysumer.cherrysumer.service.InventoryService;
import cherrysumer.cherrysumer.web.dto.InventoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    // 기존 재고 조회
    @GetMapping
    public ResponseEntity<List<Inventory>> getInventory(@AuthenticationPrincipal User user) {
        Long userId = Long.parseLong(user.getUsername());  // 현재 로그인한 사용자의 ID를 가져옴
        List<Inventory> inventories = inventoryService.findInventoryByUserId(userId);
        return ResponseEntity.ok(inventories);
    }

    // 재고 추가
    @PostMapping("/insert")
    public ResponseEntity<?> insertInventory(@AuthenticationPrincipal User user, @RequestBody InventoryDTO inventoryDTO) {
        Long userId = Long.parseLong(user.getUsername());
        inventoryService.insertInventory(userId, inventoryDTO);
        return ResponseEntity.ok("재고가 성공적으로 추가되었습니다.");
    }

    // 재고 수정
    @PostMapping("/edit/{id}")
    public ResponseEntity<?> editInventory(@PathVariable Long id, @RequestBody InventoryDTO inventoryDTO) {
        inventoryService.editInventory(id, inventoryDTO);
        return ResponseEntity.ok("재고가 성공적으로 수정되었습니다.");
    }

    // 재고 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.ok("재고가 성공적으로 삭제되었습니다.");
    }

    // 재고 검색
    @GetMapping("/search")
    public ResponseEntity<List<Inventory>> searchInventory(@RequestParam String query) {
        List<Inventory> inventories = inventoryService.searchInventory(query);
        return ResponseEntity.ok(inventories);
    }

    // 카테고리 추가
    @PostMapping("/category/{id}")
    public ResponseEntity<?> addCategoryToInventory(@PathVariable Long id, @RequestParam String categoryName) {
        inventoryService.addCategoryToInventory(id, categoryName);
        return ResponseEntity.ok("카테고리가 성공적으로 추가되었습니다.");
    }

    // 카테고리 삭제
    @DeleteMapping("/category/{id}")
    public ResponseEntity<?> removeCategoryFromInventory(@PathVariable Long id, @RequestParam String categoryName) {
        inventoryService.removeCategoryFromInventory(id, categoryName);
        return ResponseEntity.ok("카테고리가 성공적으로 삭제되었습니다.");
    }
}
