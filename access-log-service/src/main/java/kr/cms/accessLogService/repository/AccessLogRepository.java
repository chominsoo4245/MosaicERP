package kr.cms.accessLogService.repository;

import kr.cms.accessLogService.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
}
