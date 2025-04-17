package kr.cms.itemBff.client;

import kr.cms.common.dto.ApiResponse;
import kr.cms.itemBff.dto.InventoryDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class InventoryClient extends AbstractApiClient {

    public InventoryClient(WebClient.Builder webClientBuilder) {
        super("http://inventory-service", webClientBuilder);
    }

    public Mono<ApiResponse<InventoryDTO>> getInventory(Long id, String ip, String userAgent, String loginId) {
        String uri = "/inventory-service/" + id;
        return webclient.get()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<InventoryDTO>>() {
                });
    }

    public Mono<ApiResponse<List<InventoryDTO>>> getInventoryList(String ip, String userAgent, String loginId) {
        String uri = "/inventory-service/list";
        return webclient.get()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<List<InventoryDTO>>>() {
                });
    }

    public Mono<ApiResponse<String>> tryCreateInventory(String transactionId, InventoryDTO inventoryDTO, String ip, String userAgent, String loginId) {
        String uri = "/inventory-service/try/create/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .bodyValue(inventoryDTO)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> confirmCreateInventory(String transactionId, InventoryDTO inventoryDTO, String ip, String userAgent, String loginId) {
        String uri = "/inventory-service/confirm/create/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .bodyValue(inventoryDTO)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> cancelCreateInventory(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/inventory-service/cancel/create/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> tryUpdateInventory(InventoryDTO inventoryDTO, String ip, String userAgent, String loginId) {
        String uri = "/inventory-service/try/update";
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .bodyValue(inventoryDTO)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> confirmUpdateInventory(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/inventory-service/confirm/update/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> cancelUpdateInventory(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/inventory-service/cancel/update/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> tryDeleteInventory(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/inventory-service/try/delete/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> confirmDeleteInventory(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/inventory-service/confirm/delete/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> cancelDeleteInventory(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/inventory-service/cancel/delete/" + transactionId;
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
