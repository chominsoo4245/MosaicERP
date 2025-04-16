package kr.cms.itemBff.client;

import kr.cms.common.dto.ApiResponse;
import kr.cms.itemBff.dto.ItemDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ItemClient extends AbstractApiClient {

    public ItemClient(WebClient.Builder webClientBuilder) {
        super("http://item-service", webClientBuilder);
    }

    public Mono<ApiResponse<ItemDTO>> getItem(Long id, String ip, String userAgent, String loginId) {
        String uri = "/item-service/" + id;
        return webclient.get()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<ItemDTO>>() {
                });
    }

    public Mono<ApiResponse<List<ItemDTO>>> getItemList(String ip, String userAgent, String loginId) {
        String uri = "/item-service/list";
        return webclient.get()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<List<ItemDTO>>>() {
                });
    }

    public Mono<ApiResponse<String>> confirmCreateItem(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/item-service/confirm/create/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> cancelCreateItem(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/item-service/cancel/create/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> tryUpdateItem(ItemDTO itemDTO, String ip, String userAgent, String loginId) {
        String uri = "/item-service/try/update";
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .bodyValue(itemDTO)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> confirmUpdateItem(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/item-service/confirm/update/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> cancelUpdateItem(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/item-service/cancel/update/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> tryDeleteItem(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/item-service/try/delete/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> confirmDeleteItem(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/item-service/confirm/delete/" + transactionId;
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<String>>() {
                });
    }

    public Mono<ApiResponse<String>> cancelDeleteItem(String transactionId, String ip, String userAgent, String loginId) {
        String uri = "/item-service/cancel/delete/" + transactionId;
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
