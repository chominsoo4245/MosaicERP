package kr.cms.authService.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.cms.authService.dto.TokenRequest;
import kr.cms.authService.dto.UserRequest;
import kr.cms.common.dto.ApiResponse;
import kr.cms.authService.exception.InvalidTokenException;
import kr.cms.authService.service.TokenService;
import kr.cms.common.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "인증 API", description = "JWT 토큰 발급, 갱신, 폐기 API")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final TokenService tokenService;

    @PostMapping("/issue")
    public ApiResponse<TokenResponse> issue(
            @RequestBody UserRequest userRequest,
            @RequestHeader("X-Forwarded-For") String ip,
            @RequestHeader("X-User-Agent") String userAgent
    ) {
        ip = ip.split(",")[0];
        try {
            return tokenService.issueToken(userRequest, ip, userAgent);
        } catch (IllegalArgumentException e) {
            logger.warn("잘못된 입력 값: {}", e.getMessage());
            return ApiResponse.fail(e.getMessage());
        } catch (Exception e) {
            logger.error("토큰 발급 중 서버 오류", e);
            return ApiResponse.fail("토큰 발급 중 서버 오류가 발생했습니다.");
        }
    }

    @PostMapping("/refresh")
    public ApiResponse<TokenResponse> refresh(
            @RequestBody TokenRequest request,
            @RequestHeader("X-Forwarded-For") String ip,
            @RequestHeader("X-User-Agent") String userAgent,
            @RequestHeader("X-User-Id") String loginId
    ) {
        ip = ip.split(",")[0];

        try {
            return tokenService.refreshToken(request, ip, userAgent, loginId);
        } catch (InvalidTokenException e) {
            logger.warn("유효하지 않은 토큰: {}", e.getMessage());
            return ApiResponse.fail(e.getMessage());
        } catch (Exception e) {
            logger.error("토큰 갱신 중 서버 오류", e);
            return ApiResponse.fail("토큰 갱신 중 서버 오류가 발생했습니다.");
        }
    }

    @PostMapping("/revoke")
    public ApiResponse<String> revoke(
            @RequestBody TokenRequest request,
            @RequestHeader("X-Forwarded-For") String ip,
            @RequestHeader("X-User-Agent") String userAgent,
            @RequestHeader("X-User-Id") String loginId
    ) {
        ip = ip.split(",")[0];

        try {
            return tokenService.revokeToken(request, ip, userAgent, loginId);
        } catch (InvalidTokenException e) {
            logger.warn("유효하지 않은 토큰 폐기 요청: {}", e.getMessage());
            return ApiResponse.fail(e.getMessage());
        } catch (Exception e) {
            logger.error("토큰 폐기 중 서버 오류", e);
            return ApiResponse.fail("토큰 폐기 중 서버 오류가 발생했습니다.");
        }
    }
}
