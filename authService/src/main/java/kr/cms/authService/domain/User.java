package kr.cms.authService.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_user")
@Getter
@NoArgsConstructor
public class User {
    @Id
    private Long id;

    private String loginId;
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private UserRole userRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_code")
    private UserStatus userStatus;

    @Builder
    public User(String loginId, String password, UserRole userRole, UserStatus userStatus) {
        this.loginId = loginId;
        this.password = password;
        this.userRole = userRole;
        this.userStatus = userStatus;
    }
}