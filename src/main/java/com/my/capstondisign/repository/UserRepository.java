package com.my.capstondisign.repository;

import com.my.capstondisign.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(String id); //암호화되기 전과 후의 비밀번호를 비교하므로 id만 찾도록 해야함
}

