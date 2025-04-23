package kr.cms.auditLogService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_audit_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String domain;
    private String action;
    private String description;
    private String loginId;
    private String ip;
    private String userAgent;
    private LocalDateTime createdAt;
}
