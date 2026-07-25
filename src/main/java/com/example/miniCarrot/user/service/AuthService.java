package com.example.miniCarrot.user.service;

import com.example.miniCarrot.user.dto.SignUpDto;
import com.example.miniCarrot.user.entity.User;
import com.example.miniCarrot.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 이메일 중복 확인
    public void emailVerification(String email){
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
    }

    // 아이디 중복 확인
    public void loginIDVerification(String loginId) {
        if (userRepository.existsByLoginId(loginId)) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
    }

    // 회원 가입 정보 DB에 저장
    @Transactional
    public void signUp(SignUpDto signUpDto) {
        emailVerification(signUpDto.getEmail());
        loginIDVerification(signUpDto.getLoginId());

        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());

        User user = User.builder()
                .loginId(signUpDto.getLoginId())
                .password(encodedPassword)
                .email(signUpDto.getEmail())
                .build();

        userRepository.save(user);
    }
}
