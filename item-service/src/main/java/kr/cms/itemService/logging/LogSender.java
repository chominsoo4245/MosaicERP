package kr.cms.itemService.logging;

import kr.cms.common.dto.AuditLogDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogSender {

    private final KafkaTemplate<String, AuditLogDTO> kafkaTemplate;
    private static final String INVENTORY_AUDIT_LOG = "item-audit-log";

    public void sendAuditLog(AuditLogDTO dto){
        kafkaTemplate.send(INVENTORY_AUDIT_LOG, dto).whenComplete((res, ex) -> {
            if(ex != null) {
                log.error("[KAFKA] Audit Log 전송 실패", ex);
            }else {
                log.info("[KAFKA] Audit Log 전송 성공");
            }
        });
    }
}
