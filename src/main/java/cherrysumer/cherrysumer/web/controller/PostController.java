package cherrysumer.cherrysumer.web.controller;


import cherrysumer.cherrysumer.service.PostService;
import cherrysumer.cherrysumer.web.dto.PostRequestDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    // 공구 게시글 조회
    @GetMapping("")
    public ResponseEntity<?> inquiryPosts() {
        return ResponseEntity.status(HttpStatus.OK).body(postService.findPlacePosts());
    }

    // 공구 게시글 작성
    @PostMapping("")
    public ResponseEntity<?> createPost(@RequestBody PostRequestDTO.addPostDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.savePost(request));
    }


}
