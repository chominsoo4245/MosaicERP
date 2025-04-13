package kr.cms.categoryService.logging;

import kr.cms.common.dto.AuditLogDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogSender {

    private final KafkaTemplate<String, AuditLogDTO> kafkaTemplate;
    private static final String TOPIC = "category-audit-log";

    private void sendAuditLog(AuditLogDTO logDto) {
        ProducerRecord<String, AuditLogDTO> record = new ProducerRecord<>(TOPIC, logDto);
        kafkaTemplate.send(record).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("[KAFKA] Audit 로그 전송 실패", ex);
            } else {
                log.info("[KAFKA] Audit 로그 전송 성공");
            }
        });
    }

    public void sendLog(String action, String description, String ip, String userAgent, String loginId) {
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
