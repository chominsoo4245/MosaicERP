package kr.cms.auditLogService.repository;

import kr.cms.auditLogService.entity.InventoryAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryAuditLogRepository extends JpaRepository<InventoryAuditLog, Long> {
}
