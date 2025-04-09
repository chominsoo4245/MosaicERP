package kr.cms.loginService.service;

import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.AuditLogDto;
import kr.cms.loginService.dto.LoginRequest;
import kr.cms.loginService.dto.TokenRequest;
import kr.cms.loginService.dto.UserRequest;
import kr.cms.loginService.entity.User;
import kr.cms.loginService.logging.LogProducer;
import kr.cms.loginService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import kr.cms.common.dto.TokenResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WebClient authWebClient;
    private final LogProducer logProducer;

    public ApiResponse<TokenResponse> login(LoginRequest request, String ip, String userAgent) {

        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            sendFailAudit(user.getLoginId(), "비밀번호 불일치", ip, userAgent);
            return ApiResponse.fail("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        UserRequest userRequest = new UserRequest(user.getLoginId(), user.getRole().getName());

        ParameterizedTypeReference<ApiResponse<TokenResponse>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        ApiResponse<TokenResponse> response = authWebClient.post()
                .uri("/issue")
                .headers(headers -> {
                    headers.set("X-Forwarded-For", ip);
                    headers.set("X-User-Agent", userAgent);
                })
                .bodyValue(userRequest)
                .retrieve()
                .bodyToMono(typeRef)
                .block();

        if (!Objects.requireNonNull(response).isSuccess()) {
            sendFailAudit(user.getLoginId(), "토큰 발급 실패", ip, userAgent);
            return ApiResponse.fail("토큰 발급 실패");
        }

        sendSuccessAudit("LOGIN_SUCCESS", user.getLoginId(), ip, userAgent);
        return ApiResponse.success(Objects.requireNonNull(response).getData());
    }

    public ApiResponse<String> logout(TokenRequest tokenRequest, String ip, String userAgent, String loginId) {
        ParameterizedTypeReference<ApiResponse<String>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        ApiResponse<String> response = authWebClient.post()
                .uri("/revoke")
                .headers(headers -> {
                    headers.set("X-Forwarded-For", ip);
                    headers.set("X-User-Agent", userAgent);
                    headers.set("X-User-Id", loginId);
                })
                .bodyValue(tokenRequest)
                .retrieve()
                .bodyToMono(typeRef)
                .block();

        sendSuccessAudit("LOGOUT_SUCCESS", loginId, ip, userAgent);
        return ApiResponse.successWithMessage(Objects.requireNonNull(response).getMessage());
    }

    private void sendSuccessAudit(String action, String loginId, String ip, String agent) {
        logProducer.sendAuditLog(
                new AuditLogDto(
                        action,
                        loginId,
                        "정상 처리됨",
                        ip,
                        agent,
                        LocalDateTime.now()
                )
        );
    }

    private void sendFailAudit(String loginId, String reason, String ip, String agent) {
        logProducer.sendAuditLog(
                new AuditLogDto(
                        "LOGIN_FAIL",
                        loginId,
                        reason,
                        ip,
                        agent,
                        LocalDateTime.now()
                )
        );
    }
}