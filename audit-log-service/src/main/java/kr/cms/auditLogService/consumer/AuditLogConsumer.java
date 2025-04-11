package kr.cms.auditLogService.consumer;

import kr.cms.auditLogService.entity.AuthAuditLog;
import kr.cms.auditLogService.entity.InventoryAuditLog;
import kr.cms.auditLogService.entity.LoginAuditLog;
import kr.cms.auditLogService.repository.AuthAuditLogRepository;
import kr.cms.auditLogService.repository.InventoryAuditLogRepository;
import kr.cms.auditLogService.repository.LoginAuditLogRepository;
import kr.cms.common.dto.AuditLogDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditLogConsumer {

    private final AuthAuditLogRepository authAuditLogRepository;
    private final LoginAuditLogRepository loginAuditLogRepository;
    private final InventoryAuditLogRepository inventoryAuditLogRepository;

    @KafkaListener(topics = "auth-audit-log", groupId = "auth-audit-log-group")
    public void consumeAuthAudit(AuditLogDTO logDto) {
        log.info("[AUTH-AUDIT-LOG] {} {} from IP = {} | UA='{}' | message = {} | at {}",
                logDto.getAction(),
                logDto.getLoginId(),
                logDto.getIp(),
                logDto.getUserAgent(),
                logDto.getDescription(),
                logDto.getTimestamp()
        );

        AuthAuditLog logEntity = AuthAuditLog.builder()
                .loginId(logDto.getLoginId())
                .action(logDto.getAction())
                .description(logDto.getDescription())
                .ip(logDto.getIp())
                .userAgent(logDto.getUserAgent())
                .createdAt(logDto.getTimestamp())
                .build();

        authAuditLogRepository.save(logEntity);
    }

    @KafkaListener(topics = "login-audit-log", groupId = "login-audit-log-group")
    public void consumeLoginAudit(AuditLogDTO logDto){
        log.info("[LOGIN-AUDIT-LOG] {} {} from IP = {} | UA='{}' | message = {} | at {}",
                logDto.getAction(),
                logDto.getLoginId(),
                logDto.getIp(),
                logDto.getUserAgent(),
                logDto.getDescription(),
                logDto.getTimestamp()
        );

        LoginAuditLog logEntity = LoginAuditLog.builder()
                .loginId(logDto.getLoginId())
                .action(logDto.getAction())
                .description(logDto.getDescription())
                .ip(logDto.getIp())
                .userAgent(logDto.getUserAgent())
                .createdAt(logDto.getTimestamp())
                .build();

        loginAuditLogRepository.save(logEntity);
    }

    @KafkaListener(topics = "inventory-audit-log", groupId = "inventory-audit-log-group")
    public void consumeInventoryAudit(AuditLogDTO logDto){
        log.info("[INVENTORY-AUDIT-LOG] {} {} from IP = {} | UA='{}' | message = {} | at {}",
                logDto.getAction(),
                logDto.getLoginId(),
                logDto.getIp(),
                logDto.getUserAgent(),
                logDto.getDescription(),
                logDto.getTimestamp()
        );

        InventoryAuditLog logEntity = InventoryAuditLog.builder()
                .loginId(logDto.getLoginId())
                .action(logDto.getAction())
                .description(logDto.getDescription())
                .ip(logDto.getIp())
                .userAgent(logDto.getUserAgent())
                .createdAt(logDto.getTimestamp())
                .build();

        inventoryAuditLogRepository.save(logEntity);
    }
}
