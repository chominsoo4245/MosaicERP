package kr.cms.inventoryService.logging;

import kr.cms.common.dto.AuditLogDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogSender {

    private final KafkaTemplate<String, AuditLogDto> kafkaTemplate;
    private static final String INVENTORY_AUDIT_LOG = "inventory-audit-log";

    public void sendAuditLog(AuditLogDto dto){
        kafkaTemplate.send(INVENTORY_AUDIT_LOG, dto).whenComplete((res, ex) -> {
            if(ex != null) {
                log.error("[KAFKA] Audit Log 전송 실패", ex);
            }else {
                log.info("[KAFKA] Audit Log 전송 성공");
            }
        });
    }
}
