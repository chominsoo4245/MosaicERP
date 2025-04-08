package kr.cms.auditLogService.repository;

import kr.cms.auditLogService.entity.LoginAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginAuditLogRepository extends JpaRepository<LoginAuditLog, Long> {
}
