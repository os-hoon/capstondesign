package cherrysumer.cherrysumer.repository;

import cherrysumer.cherrysumer.domain.Post;
import cherrysumer.cherrysumer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUser(User user);

    @Query(value = "SELECT * FROM post p WHERE p.title LIKE %:q% OR JSON_CONTAINS(p.categories, :q)", nativeQuery = true)
    List<Post> findAllBySearchPost(@Param("q") String query);

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.place LIKE %:keyword% OR p.content LIKE %:keyword%")
    List<Post> searchByKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM post p WHERE JSON_CONTAINS(p.detailed_category, JSON_QUOTE(:keyword)) OR JSON_CONTAINS(p.category, JSON_QUOTE(:keyword))", nativeQuery = true)
    List<Post> searchByKeywordNative(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM post p WHERE p.region_code = :regionCode AND (:category IS NULL OR JSON_CONTAINS(p.category, JSON_ARRAY(:category)))", nativeQuery = true)
    List<Post> findAllPost(@Param("regionCode")String regionCode, @Param("category")List<String> category);
}
