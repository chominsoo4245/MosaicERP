package kr.cms.gateway.logging;

import kr.cms.common.dto.AccessLogDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogSender {

    private final KafkaTemplate<String, AccessLogDTO> kafkaTemplate;
    private static final String TOPIC = "api-gateway-access-log";

    public void sendAccessLog(AccessLogDTO logDto) {
        ProducerRecord<String, AccessLogDTO> record = new ProducerRecord<>(TOPIC, logDto);
        kafkaTemplate.send(record).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("[KAFKA ERROR] 로그 전송 실패", ex);
            }
        });
    }
}
