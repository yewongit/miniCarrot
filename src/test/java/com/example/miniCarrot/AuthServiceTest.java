package com.example.miniCarrot;

import com.example.miniCarrot.user.service.AuthService;
import com.example.miniCarrot.user.dto.SignUpDto;
import com.example.miniCarrot.user.entity.User;
import com.example.miniCarrot.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach; // 각 테스트 실행 직전에 실행할 준비 작업용
import org.junit.jupiter.api.DisplayName; // 테스트 이름을 한글로 예쁘게 붙이는 용도
import org.junit.jupiter.api.Test; // "이 메서드가 테스트 메서드다"라고 알려주는 역할
import org.junit.jupiter.api.extension.ExtendWith; // 외부 확장 도구(Mockito)를 붙일 때 사용
import org.mockito.InjectMocks; // assertThrows, assertDoesNotThrow 등 검증 도구
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

// 1. Mockito(가짜 객체 도구)를 사용하겠다고 선언
@ExtendWith(MockitoExtension.class)
// 2. 테스트 결과 창에 나올 이름
@DisplayName("AuthService 단위 테스트")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository; // 가짜 DB 접근 객체

    @Mock
    private PasswordEncoder passwordEncoder; // 가짜 비밀번호 암호화 객체

    @InjectMocks
    private AuthService authService; // 진짜 테스트할 대상

    private SignUpDto signUpDto;

    // 아래의 모든 @Test 메서드가 실행되기 '직전'마다 매번 1번씩 실행됨
    @BeforeEach
    void setUp() {
        signUpDto = new SignUpDto();
        signUpDto.setLoginId("testuser");
        signUpDto.setPassword("password123");
        signUpDto.setEmail("test@example.com");
    }

    @Test
    @DisplayName("정상적인 회원가입 성공")
    void testSignUpSuccess() {
        // given (시나리오 준비)
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByLoginId(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");

        // when (진짜 로직 실행)
        authService.signUp(signUpDto);

        // then (결과 검증)
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("password123");
    }

    @Test
    @DisplayName("중복 이메일로 회원가입 실패")
    void testSignUpFailureWithDuplicateEmail() {
        // given
        when(userRepository.existsByEmail(signUpDto.getEmail())).thenReturn(true);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> authService.signUp(signUpDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("중복 아이디로 회원가입 실패")
    void testSignUpFailureWithDuplicateLoginId() {
        // given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByLoginId(signUpDto.getLoginId())).thenReturn(true);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> authService.signUp(signUpDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("이메일 중복 확인 - 사용 가능")
    void testEmailVerificationSuccess() {
        // given
        when(userRepository.existsByEmail("available@example.com")).thenReturn(false);

        // when & then
        assertDoesNotThrow(() -> authService.emailVerification("available@example.com"));
    }

    @Test
    @DisplayName("이메일 중복 확인 - 중복됨")
    void testEmailVerificationFailure() {
        // given
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> authService.emailVerification("existing@example.com"));
    }

    @Test
    @DisplayName("아이디 중복 확인 - 사용 가능")
    void testLoginIdVerificationSuccess() {
        // given
        when(userRepository.existsByLoginId("availableId")).thenReturn(false);

        // when & then
        assertDoesNotThrow(() -> authService.loginIDVerification("availableId"));
    }

    @Test
    @DisplayName("아이디 중복 확인 - 중복됨")
    void testLoginIdVerificationFailure() {
        // given
        when(userRepository.existsByLoginId("existingId")).thenReturn(true);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> authService.loginIDVerification("existingId"));
    }
}
