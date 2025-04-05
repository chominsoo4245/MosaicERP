package kr.cms.loginService.service;

import kr.cms.common.dto.ApiResponse;
import kr.cms.loginService.dto.LoginRequest;
import kr.cms.loginService.dto.TokenRequest;
import kr.cms.loginService.dto.UserRequest;
import kr.cms.loginService.entity.User;
import kr.cms.loginService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import kr.cms.common.dto.TokenResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WebClient authWebClient;

    public ApiResponse<TokenResponse> login(LoginRequest request) {
        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ApiResponse.fail("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        UserRequest userRequest = new UserRequest(user.getLoginId(), user.getRole().getName());

        ParameterizedTypeReference<ApiResponse<TokenResponse>> typeRef =
                new ParameterizedTypeReference<>() {};

        ApiResponse<TokenResponse> response = authWebClient.post()
                .uri("/issue")
                .bodyValue(userRequest)
                .retrieve()
                .bodyToMono(typeRef)
                .block();
        return ApiResponse.success(Objects.requireNonNull(response).getData());
    }

    public ApiResponse<String> logout(TokenRequest tokenRequest) {
        ParameterizedTypeReference<ApiResponse<String>> typeRef =
                new ParameterizedTypeReference<>() {};

        ApiResponse<String> response = authWebClient.post()
                .uri("/revoke")
                .bodyValue(tokenRequest)
                .retrieve()
                .bodyToMono(typeRef)
                .block();

        return ApiResponse.success(Objects.requireNonNull(response).getData());
    }
}