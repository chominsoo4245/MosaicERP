package kr.cms.authService.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;
    private String password;
    private String role;
    private UserStatus userStatus;

    @Builder
    public User(String loginId, String password, String role, UserStatus userStatus) {
        this.loginId = loginId;
        this.password = password;
        this.role = role;
        this.userStatus = userStatus;
    }
}