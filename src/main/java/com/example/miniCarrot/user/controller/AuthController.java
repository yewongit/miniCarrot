package com.example.miniCarrot.user.controller;

import com.example.miniCarrot.user.service.AuthService;
import com.example.miniCarrot.user.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/email-verification")
    public ResponseEntity<String> emailVerification(@RequestParam("email") String email) {
        authService.emailVerification(email);
        return ResponseEntity.ok("사용 가능한 이메일입니다.");
    }

    @GetMapping("/loginID-verification")
    public ResponseEntity<String> loginIDVerification(@RequestParam("loginId") String loginId) {
        authService.loginIDVerification(loginId);
        return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }

    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody SignUpDto signUpDto) {
        authService.signUp(signUpDto);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
}
