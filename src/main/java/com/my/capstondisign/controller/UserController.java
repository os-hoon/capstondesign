package com.my.capstondisign.controller;

import com.my.capstondisign.vo.UserJoinRequest;
import com.my.capstondisign.vo.UserLoginRequest;
import com.my.capstondisign.model.User;
import com.my.capstondisign.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UserJoinRequest request) {
        userService.join(request);
        return ResponseEntity.ok("User successfully registered");
    }

    //postman에서 테스트할 때 비밀번호 내가 join할 때 사용했던 비밀번호 그대로 넣고 요청하면 됨 (암호화되어 저장된 비밀번호 적으면 안됨)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
        User user = userService.login(request);
        if (user != null) {
            // JWT 토큰 생성 로직 추가 예정
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
