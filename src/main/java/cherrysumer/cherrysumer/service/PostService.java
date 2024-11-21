package cherrysumer.cherrysumer.service;

import cherrysumer.cherrysumer.web.dto.PostRequestDTO;
import cherrysumer.cherrysumer.web.dto.PostResponseDTO;
import org.locationtech.jts.io.ParseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {

    PostResponseDTO.summaryPostDTO savePost(PostRequestDTO.postDTO request, List<MultipartFile> imagefile) throws ParseException, IOException;

    List<PostResponseDTO.postDTO> findRegionPosts(List<String> category, String filter);

    void deletePost(Long postId);

    PostResponseDTO.detailPostDTO updatePost(PostRequestDTO.postDTO request, List<MultipartFile> imagefile) throws ParseException, IOException;

    PostResponseDTO.detailPostDTO detailPost(Long postId);

    PostResponseDTO.summaryPostDTO applicationPost(Long postId);

    PostResponseDTO.likePostDTO likePost(Long postId);

    PostResponseDTO.closePostDTO closePost(Long postId);

    List<PostResponseDTO.participateDTO> findRecruitList();

    List<PostResponseDTO.postDataDTO> postList();

    List<PostResponseDTO.postDataDTO> postLikeList();

    List<PostResponseDTO.postDTO> searchPosts(String q, List<String> category, String filter);

    List<String> recommendKeyword();

}
