package com.example.miniCarrot.user.repository;

import com.example.miniCarrot.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    // 아이디 중복 검사
    boolean existsByLoginId(String loginId);

    // 이메일 중복 검사
    boolean existsByEmail(String email);
}
