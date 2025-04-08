package kr.cms.auditLogService.repository;

import kr.cms.auditLogService.entity.AuthAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthAuditLogRepository extends JpaRepository<AuthAuditLog, Long> {
}
