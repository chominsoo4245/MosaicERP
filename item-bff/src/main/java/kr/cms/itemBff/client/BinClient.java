package kr.cms.itemBff.client;

import kr.cms.common.dto.ApiResponse;
import kr.cms.itemBff.dto.BinDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class BinClient extends AbstractApiClient{
    public BinClient(WebClient.Builder webClientBuilder) {
        super("http://bin-service", webClientBuilder);
    }

    public Mono<ApiResponse<List<BinDTO>>> getBinList(String ip, String userAgent, String loginId) {
        String uri = "/bin-service/list";
        return webclient.get()
                .uri(uri)
                .header("X-Forwarded-For", ip)
                .header("X-User-Agent", userAgent)
                .header("X-User-Id", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<List<BinDTO>>>() {});
    }
}
