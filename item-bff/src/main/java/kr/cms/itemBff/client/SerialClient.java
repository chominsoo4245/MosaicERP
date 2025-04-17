package kr.cms.itemBff.client;

import kr.cms.common.dto.ApiResponse;
import kr.cms.itemBff.dto.SerialDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class SerialClient extends AbstractApiClient {

    public SerialClient(WebClient.Builder webClientBuilder) {
        super("http://serial-service", webClientBuilder);
    }

    public Mono<ApiResponse<SerialDTO>> getSerial(Long id, String ip, String userAgent, String loginId) {
        String uri = "/serial-service/" + id;
        return webclient.get()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<SerialDTO>>() {
                });
    }

    public Mono<ApiResponse<List<SerialDTO>>> getSerialList(String ip, String userAgent, String loginId) {
        String uri = "/serial-service/list";
        return webclient.get()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<List<SerialDTO>>>() {
                });
    }

    public Mono<ApiResponse<String>> tryCreateSerial(String transactionId, SerialDTO serialDTO, String ip, String userAgent, String loginId) {
        String uri = "/serial-service/try/create/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .bodyValue(serialDTO)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> confirmCreateSerial(String transactionId, SerialDTO serialDTO, String ip, String userAgent, String loginId) {
        String uri = "/serial-service/confirm/create/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .bodyValue(serialDTO)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> cancelCreateSerial(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/serial-service/cancel/create/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> tryUpdateSerial(String transactionId, SerialDTO serialDTO, String ip, String userAgent, String loginId) {
        String uri = "/serial-service/try/update/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .bodyValue(serialDTO)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> confirmUpdateSerial(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/serial-service/confirm/update/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> cancelUpdateSerial(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/serial-service/cancel/update/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> tryDeleteSerial(String transactionId, Long serialId, String ip, String userAgent, String loginId) {
        String uri = "/serial-service/try/delete/" + transactionId + "/" + serialId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> confirmDeleteSerial(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/serial-service/confirm/delete/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> cancelDeleteSerial(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/serial-service/cancel/delete/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }
}
