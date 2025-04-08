package kr.cms.auditLogService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_login_audit_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;
    private String action;
    private String description;
    private String ip;
    private String userAgent;
    private LocalDateTime createdAt;

}
