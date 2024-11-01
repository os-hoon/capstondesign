package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.SearchLogRedis;
import cherrysumer.cherrysumer.domain.User;
import cherrysumer.cherrysumer.exception.BaseException;
import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.repository.SearchLogRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class SearchLogServiceImpl implements SearchLogService {

    private final RedisTemplate<String, SearchLogRedis> redisTemplate;
    private final SearchLogRedisRepository searchLogRedisRepository;
    private final UserService userService;

    @Value("${app.recent-keyword-size}")
    private int RECENT_KEYWORD_SIZE;

    private String searchLogKey(Long memberId) {
        return "searchLog:" + memberId;
    }

    @Override
    public void saveRecentSearchLog(String name) {
        User user = userService.getLoggedInUser();
        String key = searchLogKey(user.getId());

        SearchLogRedis value = new SearchLogRedis();
        value.setName(name);
        value.setCreatedAt(LocalDateTime.now());


        Long size = redisTemplate.opsForList().size(key);
        if (size != null && size >= RECENT_KEYWORD_SIZE) {
            redisTemplate.opsForList().rightPop(key);
        }

        redisTemplate.opsForList().leftPush(key, value);
        searchLogRedisRepository.save(value);
    }

    @Override
    public List<SearchLogRedis> getRecentSearchLogs() {
        User user = userService.getLoggedInUser();
        String key = searchLogKey(user.getId());

        return redisTemplate.opsForList().range(key, 0, RECENT_KEYWORD_SIZE - 1);
    }

    @Override
    public void deleteRecentSearchLog(String name, LocalDateTime createdAt) {
        User user = userService.getLoggedInUser();
        String key = searchLogKey(user.getId());

        SearchLogRedis value = new SearchLogRedis();
        value.setName(name);
        value.setCreatedAt(createdAt);

        long count = redisTemplate.opsForList().remove(key, 1, value);

        if (count == 0) {
            new BaseException(ErrorCode._SEARCH_NOT_FOUND);
        }

        searchLogRedisRepository.delete(value);
    }
}
