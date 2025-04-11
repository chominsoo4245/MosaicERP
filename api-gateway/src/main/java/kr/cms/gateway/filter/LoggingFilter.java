package kr.cms.gateway.filter;

import kr.cms.common.dto.AccessLogDTO;
import kr.cms.gateway.logging.LogSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingFilter implements GlobalFilter, Ordered {

    private final LogSender logSender;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Instant start = Instant.now();

        ServerHttpRequest request = exchange.getRequest();
        String ip = extractClientIp(request);
        String userAgent = request.getHeaders().getFirst("User-Agent");
        String path = request.getURI().getPath();
        String method = request.getMethod().name();

        // 내부 서비스로 넘길 헤더 세팅
        ServerHttpRequest mutatedRequest = request.mutate()
                .headers(headers -> {
                    headers.set("X-Forwarded-For", ip); // ✅ set()으로 덮어쓰기
                    headers.set("X-User-Agent", userAgent != null ? userAgent : "UNKNOWN");
                })
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build())
                .then(Mono.fromRunnable(() -> {
                    HttpStatus status = (HttpStatus) exchange.getResponse().getStatusCode();
                    Instant end = Instant.now();

                    AccessLogDTO logDto = new AccessLogDTO();
                    logDto.setMethod(method);
                    logDto.setPath(path);
                    logDto.setIp(ip);
                    logDto.setUserAgent(userAgent);
                    logDto.setStatusCode(status != null ? status.value() : 0);
                    logDto.setResponseTimeMs(Duration.between(start, end).toMillis());
                    logDto.setTimestamp(LocalDateTime.now());

                    logSender.sendAccessLog(logDto);
                }));
    }

    private String extractClientIp(ServerHttpRequest request) {
        InetSocketAddress remote = request.getRemoteAddress();
        return remote != null ? remote.getAddress().getHostAddress() : "UNKNOWN";
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
