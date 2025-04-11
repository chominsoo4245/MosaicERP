package kr.cms.inventoryBff.client;

import kr.cms.common.dto.ApiResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public abstract class AbstractApiClient {
    protected final WebClient webclient;

    public AbstractApiClient(String baseUrl, WebClient.Builder webClientBuilder){
        this.webclient = webClientBuilder.baseUrl(baseUrl).build();
    }

    protected <T> Mono<ApiResponse<T>> get(String uri, ParameterizedTypeReference<ApiResponse<T>> typeReference) {
        return webclient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(typeReference);
    }

    protected <T, U> Mono<ApiResponse<T>> post(String uri, U body, ParameterizedTypeReference<ApiResponse<T>> typeReference) {
        return webclient.post()
                .uri(uri)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(typeReference);
    }

    protected <T, U> Mono<ApiResponse<T>> put(String uri, U body, ParameterizedTypeReference<ApiResponse<T>> typeReference) {
        return webclient.put()
                .uri(uri)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(typeReference);
    }

    protected <T> Mono<ApiResponse<T>> delete(String uri, ParameterizedTypeReference<ApiResponse<T>> typeReference) {
        return webclient.delete()
                .uri(uri)
                .retrieve()
                .bodyToMono(typeReference);
    }
}
