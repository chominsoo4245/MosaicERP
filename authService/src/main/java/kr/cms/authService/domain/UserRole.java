package kr.cms.authService.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_auth_role")
@Getter
@NoArgsConstructor
public class UserRole {
    @Id
    private long id;

    private String name;
    private String description;
}
