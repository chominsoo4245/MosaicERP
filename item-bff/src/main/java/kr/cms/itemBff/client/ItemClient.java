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

    public Mono<ApiResponse<Long>> createItem(ItemDTO body, String ip, String userAgent, String loginId){
        String uri = "/item-service/create";
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<Long>>() {});
    }
}
