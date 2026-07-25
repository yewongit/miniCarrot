package com.example.miniCarrot.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    // 기본키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    // 회원 아이디
    @Column(name = "loginId", nullable = false, unique = true)
    private String loginId;

    // 비밀번호
    @Column(name = "password", nullable = false)
    private String password;

    // 이메일
    @Column(name = "email", nullable = false, unique = true)
    private String email;
}
