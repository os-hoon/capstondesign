package cherrysumer.cherrysumer.web.controller;


import cherrysumer.cherrysumer.service.PostService;
import cherrysumer.cherrysumer.web.dto.PostRequestDTO;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> createPost(@Valid @RequestBody PostRequestDTO.addPostDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.savePost(request));
    }

    // 게시글 상세 조회
    /*@GetMapping("/{postId}")
    public ResponseEntity<?> detailPost(@PathVariable("postId") Long postId) {
        return null;
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.status(HttpStatus.OK).body("해당 게시글이 삭제되었습니다.");
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<?> putPost(@PathVariable("postId") Long postId) {
        return null;
    }

    // 게시글 검색
    @GetMapping("/search")
    public ResponseEntity<?> searchPost(@RequestParam("q") String q) {
        return null;
    }

    // 공구 참여 신청
    @PostMapping("/{postId}/participate")
    public ResponseEntity<?> participationPost(@PathVariable("postId") Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.participatePost(postId));
    }

    // 관심목록 추가, 제거
    @PutMapping("/{postId}/likes")
    public ResponseEntity<?> addlikePost(@PathVariable("postId") Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.likePost(postId));
    }*/

}
