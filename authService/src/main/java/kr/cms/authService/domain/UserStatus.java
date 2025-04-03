package kr.cms.authService.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_user_status")
@Getter
@NoArgsConstructor
public class UserStatus {
    @Id
    private String statusCode;

    private String description;
}
