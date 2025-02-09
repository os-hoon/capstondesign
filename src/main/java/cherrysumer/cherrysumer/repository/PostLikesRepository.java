package cherrysumer.cherrysumer.repository;

import cherrysumer.cherrysumer.domain.Post;
import cherrysumer.cherrysumer.domain.PostLikes;
import cherrysumer.cherrysumer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {

    Long countByPost(Post post);
    Boolean existsByPostAndUser(Post post, User user);
    PostLikes findByPostAndUser(Post post, User user);


    @Query("SELECT pl FROM PostLikes pl JOIN FETCH pl.post WHERE pl.user = :user")
    List<PostLikes> findAllByUser(@Param("user") User user);

    @Modifying
    @Query("DELETE FROM PostLikes p where p.post = :post")
    void deleteAlllikes(@Param("post") Post post);

    @Modifying
    @Query("DELETE FROM PostLikes p where p.user = :user")
    void deleteAlllikesUser(@Param("user") User user);
}
