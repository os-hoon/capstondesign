package cherrysumer.cherrysumer.web.controller;


import cherrysumer.cherrysumer.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/mypage")
public class MyPageController {

    private final PostService postService;

    @GetMapping("/applications/{status}")
    public ResponseEntity<?> applicationList(@PathVariable(name = "status") int status) {
        // 0 = 모집, 1 = 참여
        if(status == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(postService.findRecruitList());
        } else if(status == 1) {
            return ResponseEntity.status(HttpStatus.OK).body(postService.findApplicationList());
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다.");
    }
}
