package kr.cms.loginService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_user_status")
@Getter
@Setter
public class UserStatus {
    @Id
    private int id;

    private String statusCode;
}
