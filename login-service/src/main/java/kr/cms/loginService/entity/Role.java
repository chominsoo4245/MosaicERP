package kr.cms.loginService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_auth_role")
@Getter
@Setter
public class Role {
    @Id
    private String statusCode;
}
