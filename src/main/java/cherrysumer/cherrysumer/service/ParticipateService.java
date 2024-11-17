package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.domain.Post;
import cherrysumer.cherrysumer.domain.User;
import cherrysumer.cherrysumer.web.dto.PostRequestDTO;
import cherrysumer.cherrysumer.web.dto.PostResponseDTO;
import cherrysumer.cherrysumer.web.dto.UserRequestDTO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ParticipateService {

    List<PostResponseDTO.participateDTO> findApplicationList();

    List<PostResponseDTO.participateUserDTO> participateList(Long postId, String filter);

    void updateParticipate(UserRequestDTO.decideUserDTO request);

    void closeRecruit(Post post);

    void registerInventory(Long postId);
}
