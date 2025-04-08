package kr.cms.accessLogService.consumer;

import kr.cms.accessLogService.entity.AccessLog;
import kr.cms.accessLogService.repository.AccessLogRepository;
import kr.cms.common.dto.AccessLogDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessLogConsumer {

    private final AccessLogRepository accessLogRepository;

    @KafkaListener(topics = {
            "api-gateway-access-log",
    }, groupId = "access-log-group")
    public void listen(AccessLogDto logDto) {
        log.info("[ACCESS-LOG] {} {} from IP = {} | UA='{}' | status = {} | {} ms",
                logDto.getMethod(),
                logDto.getPath(),
                logDto.getIp(),
                logDto.getUserAgent(),
                logDto.getStatusCode(),
                logDto.getResponseTimeMs()
        );

        AccessLog logEntity = AccessLog.builder()
                .serviceName(extractServiceName(logDto.getPath()))
                .method(logDto.getMethod())
                .path(logDto.getPath())
                .ip(logDto.getIp())
                .userAgent(logDto.getUserAgent())
                .statusCode(logDto.getStatusCode())
                .responseTimeMs(logDto.getResponseTimeMs())
                .createdAt(logDto.getTimestamp())
                .build();

        accessLogRepository.save(logEntity);
    }

    private String extractServiceName(String path) {
        if (path == null || !path.startsWith("/")) return "unknown";
        String[] parts = path.split("/");
        return parts.length > 1 ? parts[1] : "unknown";
    }

}
