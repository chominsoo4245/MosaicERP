package kr.cms.inventoryBff.client;

import kr.cms.common.dto.ApiResponse;
import kr.cms.inventoryBff.dto.InventoryDTO;
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

    public Mono<ApiResponse<List<InventoryDTO>>> getInventoryList(String ip, String userAgent, String loginId) {
        String uri = "/inventory-service/list";
        return webclient.get()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<List<InventoryDTO>>>() {});
    }
}
