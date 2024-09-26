package cherrysumer.cherrysumer.repository;

import cherrysumer.cherrysumer.domain.Participate;
import cherrysumer.cherrysumer.domain.Post;
import cherrysumer.cherrysumer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParticipateRepository extends JpaRepository<Participate, Long> {

    //@Query("select P.post from Participate p where P.post = :p")
    //List<Post> findAllByPost(@Param("p") Post post);

    Long countAllByPost(Post post);

    //@Query("select P.post from Participate p where P.user = :u")
    //List<Post> findAllByUser(@Param("u") User user);

    List<Participate> findAllByUser(User user);

    List<Participate> findAllByPost(Post post);
}
