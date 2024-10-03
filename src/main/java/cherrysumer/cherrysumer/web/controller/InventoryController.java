package cherrysumer.cherrysumer.web.controller;

import cherrysumer.cherrysumer.domain.Inventory;
import cherrysumer.cherrysumer.service.InventoryService;
import cherrysumer.cherrysumer.util.ApiResponse;
import cherrysumer.cherrysumer.web.dto.InventoryDTO;
import jakarta.validation.Valid;
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
    public ApiResponse<List<Inventory>> getInventory(@AuthenticationPrincipal User user) {
        Long userId = Long.parseLong(user.getUsername());  // 현재 로그인한 사용자의 ID를 가져옴
        List<Inventory> inventories = inventoryService.findInventoryByUserId(userId);
        return ApiResponse.onSuccess(inventories);
    }

    // 재고 추가
    @PostMapping("/insert")
    public ApiResponse<?> insertInventory(@AuthenticationPrincipal User user, @RequestBody @Valid InventoryDTO inventoryDTO) {
        Long userId = Long.parseLong(user.getUsername());
        inventoryService.insertInventory(userId, inventoryDTO);
        return ApiResponse.onSuccess("재고가 성공적으로 추가되었습니다.");
    }

    // 재고 수정
    @PostMapping("/edit/{id}")
    public ApiResponse<?> editInventory(@PathVariable Long id, @RequestBody @Valid InventoryDTO inventoryDTO) {
        inventoryService.editInventory(id, inventoryDTO);
        return ApiResponse.onSuccess("재고가 성공적으로 수정되었습니다.");
    }

    // 재고 삭제
    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ApiResponse.onSuccess("재고가 성공적으로 삭제되었습니다.");
    }

    // 재고 검색
    @GetMapping("/search")
    public ApiResponse<List<Inventory>> searchInventory(@RequestParam String query) {
        List<Inventory> inventories = inventoryService.searchInventory(query);
        return ApiResponse.onSuccess(inventories);
    }

    // 카테고리 추가
    @PostMapping("/category/{id}")
    public ApiResponse<?> addCategoryToInventory(@PathVariable Long id, @RequestParam String categoryName) {
        inventoryService.addCategoryToInventory(id, categoryName);
        return ApiResponse.onSuccess("카테고리가 성공적으로 추가되었습니다.");
    }

}
