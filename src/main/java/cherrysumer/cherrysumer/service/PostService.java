package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.web.dto.PostRequestDTO;
import cherrysumer.cherrysumer.web.dto.PostResponseDTO;
import org.locationtech.jts.io.ParseException;

import java.util.List;

public interface PostService {

    PostResponseDTO.summaryPostDTO savePost(PostRequestDTO.postDTO request) throws ParseException;

    List<PostResponseDTO.postDTO> findRegionPosts(List<String> category1, String category2);

    void deletePost(Long postId);

    PostResponseDTO.detailPostDTO updatePost(PostRequestDTO.postDTO request) throws ParseException;

    PostResponseDTO.detailPostDTO detailPost(Long postId);

    PostResponseDTO.summaryPostDTO applicationPost(Long postId);

    PostResponseDTO.likePostDTO likePost(Long postId);

    PostResponseDTO.closePostDTO closePost(Long postId);

    List<PostResponseDTO.recruitDTO> findRecruitList();

    List<PostResponseDTO.postDataDTO> postList();

    List<PostResponseDTO.postDataDTO> postLikeList();

    List<PostResponseDTO.postDTO> searchPosts(String q);

}
