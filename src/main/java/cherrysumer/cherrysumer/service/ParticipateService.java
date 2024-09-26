package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.web.dto.PostRequestDTO;
import cherrysumer.cherrysumer.web.dto.PostResponseDTO;

import java.util.List;

public interface ParticipateService {

    List<PostResponseDTO.recruitDTO> findRecruitList();

    List<PostResponseDTO.applicationDTO> findApplicationList();

    List<PostResponseDTO.participateUserDTO> participateList(Long postId);
}
