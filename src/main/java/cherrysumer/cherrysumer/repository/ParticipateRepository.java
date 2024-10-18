package cherrysumer.cherrysumer.repository;

import cherrysumer.cherrysumer.domain.Participate;
import cherrysumer.cherrysumer.domain.Post;
import cherrysumer.cherrysumer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipateRepository extends JpaRepository<Participate, Long> {

    //@Query("select P.post from Participate p where P.post = :p")
    //List<Post> findAllByPost(@Param("p") Post post);

    Long countAllByPost(Post post);

    //@Query("select P.post from Participate p where P.user = :u")
    //List<Post> findAllByUser(@Param("u") User user);

    List<Participate> findAllByUser(User user);

    List<Participate> findAllByPost(Post post);

    @Query("SELECT p FROM Participate p WHERE p.status = '승인'")
    List<Participate> findAllByPost0(Post post);

    @Query("SELECT p FROM Participate p WHERE p.status = '거절'")
    List<Participate> findAllByPost1(Post post);

    boolean existsByPostAndUser(Post post, User user);

    Optional<Participate> findByPostAndUser(Post post, User user);

    @Query("SELECT count(p) FROM Participate p WHERE p.status = '승인'")
    Long countParticipateByPost(Post post);

    @Query("SELECT p FROM Participate p WHERE p.post = :post and p.status = '미확인'")
    List<Participate> searchByConfirm(@Param("post") Post post);

    @Modifying
    @Query("DELETE FROM Participate p where p.post = :post")
    void deleteAllparticipate(@Param("post") Post post);
}
