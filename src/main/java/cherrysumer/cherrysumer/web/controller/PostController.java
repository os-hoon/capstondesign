package cherrysumer.cherrysumer.web.controller;


import cherrysumer.cherrysumer.service.PostService;
import cherrysumer.cherrysumer.util.ApiResponse;
import cherrysumer.cherrysumer.web.dto.PostRequestDTO;
import cherrysumer.cherrysumer.web.dto.ProfileDTO;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    // 공구 게시글 조회
    @GetMapping("")
    public ApiResponse<?> inquiryPosts(@RequestParam(value = "category") String category,
                                       @RequestParam("filter") String filter) {
        List<String> categorys = (category.equals("") || category.equals("전체")) ? null :
                Arrays.stream(category.split(","))
                .map(String::trim) // 각 요소의 앞뒤 공백 제거
                .collect(Collectors.toList());
        return ApiResponse.onSuccess(postService.findRegionPosts(categorys, filter));
    }

    // 공구 게시글 작성
    @PostMapping("")
    public ApiResponse<?> createPost(@Valid @RequestPart(value = "request") PostRequestDTO.postDTO request,
                                     @RequestPart("file") List<MultipartFile> imagefile) throws ParseException, IOException {
        return ApiResponse.onSuccess(postService.savePost(request, imagefile));
    }

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public ApiResponse<?> detailPost(@PathVariable("postId") Long postId) {
        return ApiResponse.onSuccess(postService.detailPost(postId));
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ApiResponse<?> deletePost(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
        return ApiResponse.onSuccess("해당 게시글이 삭제되었습니다.");
    }

    // 게시글 수정
    @PutMapping("")
    public ApiResponse<?> updatePost(@Valid @RequestPart(value = "request") PostRequestDTO.postDTO request,
                                     @RequestPart("file") List<MultipartFile> imagefile) throws ParseException, IOException {
        return ApiResponse.onSuccess(postService.updatePost(request, imagefile));
    }

    // 게시글 검색
    @GetMapping("/search")
    public ApiResponse<?> searchPost(@RequestParam("q") String q, @RequestParam(value = "category") String category,
                                     @RequestParam("filter") String filter) {
        List<String> categorys = (category.equals("") || category.equals("전체")) ? null :
                Arrays.stream(category.split(","))
                        .map(String::trim) // 각 요소의 앞뒤 공백 제거
                        .collect(Collectors.toList());
        return ApiResponse.onSuccess(postService.searchPosts(q, categorys, filter));
    }

    // 공구 참여 신청
    @PostMapping("/{postId}/application")
    public ApiResponse<?> applicationPost(@PathVariable("postId") Long postId) {
        return ApiResponse.onSuccess(postService.applicationPost(postId));
    }

    // 관심목록 추가, 제거
    @PutMapping("/{postId}/likes")
    public ApiResponse<?> addlikePost(@PathVariable("postId") Long postId) {
        return ApiResponse.onSuccess(postService.likePost(postId));
    }

    // 공구 모집 마감
    @PutMapping("/{postId}/closed")
    public ApiResponse<?> closePost(@PathVariable("postId") Long postId) {
        return ApiResponse.onSuccess(postService.closePost(postId));
    }

}
