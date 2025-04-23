package kr.cms.auditLogService.consumer;

import kr.cms.auditLogService.entity.AuditLog;
import kr.cms.auditLogService.repository.AuditLogRepository;
import kr.cms.common.dto.AuditLogDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditLogConsumer {

    private final AuditLogRepository auditLogRepository;
// master

    @KafkaListener(topics = "category-audit-log", groupId = "category-audit-log-group")
    public void consumeCategoryAudit(AuditLogDTO logDto) {
        String domain = "category";
        printLogInfo(domain.toUpperCase(), logDto);
        saveEntity(domain, logDto);
    }

    @KafkaListener(topics = "item-audit-log", groupId = "item-audit-log-group")
    public void consumeItemAudit(AuditLogDTO logDto) {
        String domain = "item";
        printLogInfo(domain.toUpperCase(), logDto);
        saveEntity(domain, logDto);
    }

    @KafkaListener(topics = "supplier-audit-log", groupId = "supplier-audit-log-group")
    public void consumeSupplierAudit(AuditLogDTO logDto) {
        String domain = "supplier";
        printLogInfo(domain.toUpperCase(), logDto);
        saveEntity(domain, logDto);
    }

    @KafkaListener(topics = "warehouse-audit-log", groupId = "warehouse-audit-log-group")
    public void consumeWarehouseAudit(AuditLogDTO logDto) {
        String domain = "warehouse";
        printLogInfo(domain.toUpperCase(), logDto);
        saveEntity(domain, logDto);
    }

    @KafkaListener(topics = "auth-audit-log", groupId = "auth-audit-log-group")
    public void consumeAuthAudit(AuditLogDTO logDto) {
        String domain = "auth";
        printLogInfo(domain.toUpperCase(), logDto);
        saveEntity(domain, logDto);
    }

    @KafkaListener(topics = "login-audit-log", groupId = "login-audit-log-group")
    public void consumeLoginAudit(AuditLogDTO logDto) {
        String domain = "login";
        printLogInfo(domain.toUpperCase(), logDto);
        saveEntity(domain, logDto);
    }

    @KafkaListener(topics = "inventory-audit-log", groupId = "inventory-audit-log-group")
    public void consumeInventoryAudit(AuditLogDTO logDto) {
        String domain = "inventory";
        printLogInfo(domain.toUpperCase(), logDto);
        saveEntity(domain, logDto);
    }

    @KafkaListener(topics = "bin-audit-log", groupId = "bin-audit-log-group")
    public void consumeBinAudit(AuditLogDTO logDto) {
        String domain = "bin";
        printLogInfo(domain.toUpperCase(), logDto);
        saveEntity(domain, logDto);
    }

    @KafkaListener(topics = "lot-audit-log", groupId = "lot-audit-log-group")
    public void consumeLotAudit(AuditLogDTO logDto) {
        String domain = "lot";
        printLogInfo(domain.toUpperCase(), logDto);
        saveEntity(domain, logDto);
    }

    @KafkaListener(topics = "serial-audit-log", groupId = "serial-audit-log-group")
    public void consumeSerialAudit(AuditLogDTO logDto) {
        String domain = "serial";
        printLogInfo(domain.toUpperCase(), logDto);
        saveEntity(domain, logDto);
    }

    private void printLogInfo(String domain, AuditLogDTO logDTO) {
        log.info("[{}-AUDIT-LOG] {} {} from IP = {} | UA='{}' | message = {} | at {}",
                domain,
                logDTO.getAction(),
                logDTO.getLoginId(),
                logDTO.getIp(),
                logDTO.getUserAgent(),
                logDTO.getDescription(),
                logDTO.getTimestamp()
        );
    }

    private void saveEntity(String domain, AuditLogDTO logDTO) {
        AuditLog logEntity = AuditLog.builder()
                .domain(domain + "-service")
                .loginId(logDTO.getLoginId())
                .action(logDTO.getAction())
                .description(logDTO.getDescription())
                .ip(logDTO.getIp())
                .userAgent(logDTO.getUserAgent())
                .createdAt(logDTO.getTimestamp())
                .build();
        auditLogRepository.save(logEntity);
    }
}
