package kr.cms.loginService.logging;

import kr.cms.common.dto.AccessLogDto;
import kr.cms.common.dto.AuditLogDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String AUDIT_LOG_TOPIC = "login-audit-log";

    public void sendAuditLog(AuditLogDto dto) {
        kafkaTemplate.send(AUDIT_LOG_TOPIC, dto).whenComplete((res, ex) -> {
            if(ex != null) {
                log.error("[KAFKA] Audit Log 전송 실패", ex);
            }else {
                log.info("[KAFKA] Audit Log 전송 성공");
            }
        });
    }
}
