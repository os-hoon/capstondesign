package cherrysumer.cherrysumer.web.controller;

import cherrysumer.cherrysumer.service.MyPageService;
import cherrysumer.cherrysumer.service.PostService;
import cherrysumer.cherrysumer.web.dto.ProfileDTO;
import cherrysumer.cherrysumer.web.dto.RegionDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/mypage")
public class MyPageController {

    private final PostService postService;
    private final MyPageService myPageService;

    @GetMapping("/applications/{status}")
    public ResponseEntity<?> applicationList(@PathVariable(name = "status") int status) {
        // 0 = 모집, 1 = 참여
        if (status == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(postService.findRecruitList());
        } else if (status == 1) {
            return ResponseEntity.status(HttpStatus.OK).body(postService.findApplicationList());
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다.");
    }

    // 프로필 조회
    @GetMapping("/profile/check")
    public ResponseEntity<ProfileDTO> getProfile(@AuthenticationPrincipal User user) {
        Long userId = Long.parseLong(user.getUsername());
        ProfileDTO profile = myPageService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }

    // 프로필 수정
    @PostMapping("/profile/check/modify")
    public ResponseEntity<?> modifyProfile(@AuthenticationPrincipal User user, @RequestBody ProfileDTO profileDTO) {
        Long userId = Long.parseLong(user.getUsername());
        myPageService.modifyProfile(userId, profileDTO);
        return ResponseEntity.ok("프로필이 성공적으로 수정되었습니다.");
    }

    // 내 동네 설정
    @PostMapping("/region")
    public ResponseEntity<?> setRegion(@AuthenticationPrincipal User user, @RequestBody RegionDTO regionDTO) {
        Long userId = Long.parseLong(user.getUsername());
        myPageService.setRegion(userId, regionDTO);
        return ResponseEntity.ok("동네가 성공적으로 설정되었습니다.");
    }

    // 공지사항 조회
    @GetMapping("/announcement")
    public ResponseEntity<?> getAnnouncements() {
        return ResponseEntity.ok(myPageService.getAnnouncements());
    }
}
