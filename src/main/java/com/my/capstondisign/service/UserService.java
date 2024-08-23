package com.my.capstondisign.service;

import com.my.capstondisign.vo.UserJoinRequest;
import com.my.capstondisign.vo.UserLoginRequest;
import com.my.capstondisign.model.User;
import com.my.capstondisign.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void join(UserJoinRequest request) {
        User user = new User();
        user.setId(request.getId());
        user.setPasswd(passwordEncoder.encode(request.getPasswd()));
        user.setName(request.getName());
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setCategory(request.getCategory());
        user.setRegion(request.getRegion());
        userRepository.save(user);
    }


    public User login(UserLoginRequest request) {
        // 사용자 ID로 데이터베이스에서 사용자 검색
        User user = userRepository.findById(request.getId());

        if (user != null) {
            // 비밀번호 매칭을 위해 passwordEncoder 사용
            if (passwordEncoder.matches(request.getPasswd(), user.getPasswd())) {
                // 로그인 성공
                return user;
            }
        }
        // 로그인 실패
        return null;
    }
}
