package kr.cms.itemBff.client;

import kr.cms.common.dto.ApiResponse;
import kr.cms.itemBff.dto.CategoryDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static com.sun.java.accessibility.util.EventID.ITEM;

@Component
public class CategoryClient extends AbstractApiClient{
    private static Map<String, String> BODY = Map.of("categoryType", "ITEM");
    public CategoryClient(WebClient.Builder webClientBuilder) {
        super("http://category-service", webClientBuilder);
    }

    public Mono<ApiResponse<List<CategoryDTO>>> getCategoryList(String ip, String userAgent, String loginId) {
        String uri = "/category-service/search";
        return webclient.post()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .bodyValue(BODY)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<List<CategoryDTO>>>() {});
    }

    public Mono<ApiResponse<CategoryDTO>> getCategory(Integer categoryId, String ip, String userAgent, String loginId) {
        String uri = "/category-service/" + categoryId;
        return webclient.get()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<CategoryDTO>>() {});
    }
}
