package cherrysumer.cherrysumer.repository;

import cherrysumer.cherrysumer.domain.Post;
import cherrysumer.cherrysumer.domain.PostLikes;
import cherrysumer.cherrysumer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {

    Long countByPost(Post post);
    Boolean existsByPostAndUser(Post post, User user);
    PostLikes findByPostAndUser(Post post, User user);

    List<PostLikes> findAllByUser(User user);
}
