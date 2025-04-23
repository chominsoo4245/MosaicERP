package kr.cms.auditLogService.repository;

import kr.cms.auditLogService.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
