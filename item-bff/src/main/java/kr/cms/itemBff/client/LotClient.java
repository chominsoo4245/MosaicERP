package kr.cms.itemBff.client;

import kr.cms.common.dto.ApiResponse;
import kr.cms.itemBff.dto.LotDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class LotClient extends AbstractApiClient {

    public LotClient(WebClient.Builder webClientBuilder) {
        super("http://lot-service", webClientBuilder);
    }

    public Mono<ApiResponse<LotDTO>> getLot(Long id, String ip, String userAgent, String loginId) {
        String uri = "/lot-service/" + id;
        return webclient.get()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<LotDTO>>() {
                });
    }

    public Mono<ApiResponse<List<LotDTO>>> getLotList(String ip, String userAgent, String loginId) {
        String uri = "/lot-service/list";
        return webclient.get()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<List<LotDTO>>>() {
                });
    }

    public Mono<ApiResponse<String>> tryCreateLot(String transactionId, LotDTO lotDTO, String ip, String userAgent, String loginId) {
        String uri = "/lot-service/try/create/" + transactionId ;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .bodyValue(lotDTO)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> confirmCreateLot(String transactionId, LotDTO lotDTO ,String ip, String userAgent, String loginId) {
        String uri = "/lot-service/confirm/create/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .bodyValue(lotDTO)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> cancelCreateLot(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/lot-service/cancel/create/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> tryUpdateLot(String transactionId,LotDTO lotDTO, String ip, String userAgent, String loginId) {
        String uri = "/lot-service/try/update/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .bodyValue(lotDTO)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> confirmUpdateLot(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/lot-service/confirm/update/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> cancelUpdateLot(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/lot-service/cancel/update/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> tryDeleteLot(String transactionId, Long lotId, String ip, String userAgent, String loginId) {
        String uri = "/lot-service/try/delete/" + transactionId + "/" + lotId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> confirmDeleteLot(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/lot-service/confirm/delete/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> cancelDeleteLot(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/lot-service/cancel/delete/" + transactionId;
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
