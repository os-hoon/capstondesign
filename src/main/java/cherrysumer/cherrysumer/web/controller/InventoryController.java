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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    /*
    // 기존 재고 조회
    @GetMapping
    public ApiResponse<List<Inventory>> getInventory(@AuthenticationPrincipal User user) {
        Long userId = Long.parseLong(user.getUsername());  // 현재 로그인한 사용자의 ID를 가져옴
        List<Inventory> inventories = inventoryService.findInventoryByUserId(userId);
        return ApiResponse.onSuccess(inventories);
    }*/

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

    @GetMapping("/location/{stockLocation}")
    public ApiResponse<List<Inventory>> getInventoryByLocation(@AuthenticationPrincipal User user, @PathVariable String stockLocation) {
        Long userId = Long.parseLong(user.getUsername());
        List<Inventory> inventories = inventoryService.findInventoryByStockLocationAndUserId(stockLocation, userId);
        return ApiResponse.onSuccess(inventories);
    }

    @GetMapping
    public ApiResponse<?> getFilteredInventory(@RequestParam(value = "category", required = false) String category,
                                               @RequestParam("filter") String filter) {
        List<String> categories = (category == null || category.equals("")) ? null :
                Arrays.stream(category.split(","))
                        .map(String::trim)
                        .collect(Collectors.toList());

        return ApiResponse.onSuccess(inventoryService.findFilteredInventory(categories, filter));
    }



}
