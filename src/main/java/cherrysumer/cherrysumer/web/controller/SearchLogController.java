package cherrysumer.cherrysumer.web.controller;

import cherrysumer.cherrysumer.service.SearchLogService;
import cherrysumer.cherrysumer.util.ApiResponse;
import cherrysumer.cherrysumer.web.dto.SearchLogRedisDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search-log")
@RequiredArgsConstructor
public class SearchLogController {

    private final SearchLogService searchLogService;
    /*
    @PostMapping("/save")
    public ApiResponse<?> saveSearchLog(@RequestBody SearchLogRedisDto searchLogRedisDto) {
        searchLogService.saveRecentSearchLog(searchLogRedisDto.getName());
        return ApiResponse.onSuccess("검색 기록이 성공적으로 추가되었습니다.");
    }*/

    @GetMapping("/recent")
    public ApiResponse<List<SearchLogRedisDto>> getRecentSearchLogs() {
        List<SearchLogRedisDto> logs = searchLogService.getRecentSearchLogs().stream()
                .map(log -> new SearchLogRedisDto(log.getName(), log.getCreatedAt(), log.getState()))
                .collect(Collectors.toList());
        return ApiResponse.onSuccess(logs);
    }

    @DeleteMapping("/delete")
    public ApiResponse<?> deleteSearchLog(@RequestBody SearchLogRedisDto searchLogRedisDto) {
        searchLogService.deleteRecentSearchLog(searchLogRedisDto.getName(), searchLogRedisDto.getCreatedAt(), searchLogRedisDto.getState());
        return ApiResponse.onSuccess("검색 기록이 성공적으로 삭제되었습니다.");
    }
}
