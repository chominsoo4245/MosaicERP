package kr.cms.binService.logging;

import kr.cms.common.dto.AuditLogDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogSender {

    private final KafkaTemplate<String, AuditLogDTO> kafkaTemplate;
    private static final String BIN_AUDIT_LOG = "bin-audit-log";

    private void sendAuditLog(AuditLogDTO dto){
        kafkaTemplate.send(BIN_AUDIT_LOG, dto).whenComplete((res, ex) -> {
            if(ex != null) {
                log.error("[KAFKA] Audit Log 전송 실패", ex);
            }else {
                log.info("[KAFKA] Audit Log 전송 성공");
            }
        });
    }

    public void sendLog(String action, String description, String ip, String userAgent, String loginId){
        AuditLogDTO auditLogDTO = new AuditLogDTO();
        auditLogDTO.setAction(action);
        auditLogDTO.setDescription(description);
        auditLogDTO.setIp(ip);
        auditLogDTO.setUserAgent(userAgent);
        auditLogDTO.setLoginId(loginId);
        auditLogDTO.setTimestamp(LocalDateTime.now());
        sendAuditLog(auditLogDTO);
    }
}
