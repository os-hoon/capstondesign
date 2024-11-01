package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.SearchLogRedis;

import java.time.LocalDateTime;
import java.util.List;

public interface SearchLogService {
    void saveRecentSearchLog(String name);

    List<SearchLogRedis> getRecentSearchLogs();

    void deleteRecentSearchLog(String name, LocalDateTime createdAt);
}
