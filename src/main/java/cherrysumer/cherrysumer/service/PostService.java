package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.web.dto.PostRequestDTO;
import cherrysumer.cherrysumer.web.dto.PostResponseDTO;

import java.util.List;

public interface PostService {

    PostResponseDTO.successPostDTO savePost(PostRequestDTO.addPostDTO request);

    List<PostResponseDTO.postDTO> findPlacePosts();

    void deletePost(Long postId);

    PostResponseDTO.successPostDTO participatePost(Long postId);

    PostResponseDTO.likePostDTO likePost(Long postId);
}
