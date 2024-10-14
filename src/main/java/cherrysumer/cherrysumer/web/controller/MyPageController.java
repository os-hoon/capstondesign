package cherrysumer.cherrysumer.web.controller;

import cherrysumer.cherrysumer.exception.ErrorCode;
import cherrysumer.cherrysumer.exception.handler.CertificationHandler;
import cherrysumer.cherrysumer.service.MyPageService;
import cherrysumer.cherrysumer.service.PostService;
import cherrysumer.cherrysumer.util.ApiResponse;
import cherrysumer.cherrysumer.web.dto.ProfileDTO;
import cherrysumer.cherrysumer.web.dto.RegionDTO;

import cherrysumer.cherrysumer.service.ParticipateService;
import cherrysumer.cherrysumer.web.dto.UserRequestDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/mypage")
public class MyPageController {

    private final PostService postService;
    private final MyPageService myPageService;
    private final ParticipateService participateService;

    // 모집, 참여 현황 조회
    @GetMapping("/applications/{filter}")
    public ApiResponse<?> applicationList(@PathVariable(name = "filter") String filter) {
        // 0 = 모집, 1 = 참여
       if(filter.equals("모집")) {
            return ApiResponse.onSuccess(postService.findRecruitList());
        } else if(filter.equals("참여")) {
            return ApiResponse.onSuccess(participateService.findApplicationList());
        } else
            throw new CertificationHandler(ErrorCode._BAD_REQUEST);
    }

    // 프로필 조회
    @GetMapping("/profile")
    public ApiResponse<ProfileDTO> getProfile(@AuthenticationPrincipal User user) {
        Long userId = Long.parseLong(user.getUsername());
        ProfileDTO profile = myPageService.getProfile(userId);
        return ApiResponse.onSuccess(profile);
    }

    // 프로필 수정
    @PostMapping("/profile/modify")
    public ApiResponse<?> modifyProfile(@AuthenticationPrincipal User user,
                                        @RequestPart(value = "dto") ProfileDTO profileDTO,
                                        @RequestPart("file") MultipartFile file) {
        Long userId = Long.parseLong(user.getUsername());
        myPageService.modifyProfile(userId, profileDTO,file);
        return ApiResponse.onSuccess("프로필이 성공적으로 수정되었습니다.");
    }

    // 내 동네 설정
    @PostMapping("/region")
    public ApiResponse<?> setRegion(@AuthenticationPrincipal User user, @RequestBody RegionDTO regionDTO) {
        Long userId = Long.parseLong(user.getUsername());
        myPageService.setRegion(userId, regionDTO);
        return ApiResponse.onSuccess("동네가 성공적으로 설정되었습니다.");
    }

    // 공지사항 조회
    @GetMapping("/announcement")
    public ApiResponse<?> getAnnouncements() {
        return ApiResponse.onSuccess(myPageService.getAnnouncements());
    }

    // 참여 신청자 목록 조회
    @GetMapping("/participations/{postId}")
    public ApiResponse<?> participateList(@PathVariable(name = "postId") Long postId,
                                          @RequestParam(name = "filter") String filter) {
        return ApiResponse.onSuccess(participateService.participateList(postId, filter));
    }

    // 참여 신청자 승인, 거절
    @PostMapping("/participations/decide")
    public ApiResponse<?> decideApplication(@RequestBody UserRequestDTO.decideUserDTO request) {
        participateService.updateParticipate(request);
        return ApiResponse.onSuccess("요청을 처리하였습니다.");
    }

    // 공구 관심 목록 조회
    @GetMapping("/posts/likes")
    public ApiResponse<?> likesPost() {
        return ApiResponse.onSuccess(postService.postLikeList());
    }

    // 게시글 관리
    @GetMapping("/posts")
    public ApiResponse<?> postList() {
        return ApiResponse.onSuccess(postService.postList());
    }
}
