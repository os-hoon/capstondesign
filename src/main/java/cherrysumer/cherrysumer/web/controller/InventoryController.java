package cherrysumer.cherrysumer.web.controller;

import cherrysumer.cherrysumer.domain.Inventory;
import cherrysumer.cherrysumer.service.InventoryService;
import cherrysumer.cherrysumer.service.ParticipateService;
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
    private final ParticipateService participateService;


    // 기존 재고 조회
    @GetMapping("/check")
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
    public ApiResponse<List<Inventory>> searchInventory(@AuthenticationPrincipal User user,@RequestParam String query) {
        Long userId = Long.parseLong(user.getUsername());
        List<Inventory> inventories = inventoryService.searchInventory(query, userId);
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
        // 카테고리가 선택되지 않았을 경우 null 처리
        String selectedCategory = (category == null || category.equals("")) ? null : category.trim();

        return ApiResponse.onSuccess(inventoryService.findFilteredInventory(selectedCategory, filter));
    }

    @GetMapping("/filter")
    public ApiResponse<?> getFilteredInventoryWithStockLocation(@RequestParam(value = "category", required = false) String category,
                                                                @RequestParam("filter") String filter,
                                                                @RequestParam(value = "stockLocation", required = false) String stockLocation) {
        // category와 stockLocation이 선택되지 않았을 경우 null 처리
        String selectedCategory = (category == null || category.equals("")) ? null : category.trim();
        String selectedStockLocation = (stockLocation == null || stockLocation.equals("")) ? null : stockLocation.trim();

        // 서비스에서 필터링된 결과를 가져옵니다.
        return ApiResponse.onSuccess(inventoryService.findFilteredInventoryWithStockLocation(selectedCategory, filter, selectedStockLocation));
    }


    @GetMapping("/register/{postId}")
    public ApiResponse<?> registerPostInventory(@PathVariable(name = "postId") Long postId) {
        participateService.registerInventory(postId);
        return ApiResponse.onSuccess("재고 등록이 완료되었습니다.");
    }
}
