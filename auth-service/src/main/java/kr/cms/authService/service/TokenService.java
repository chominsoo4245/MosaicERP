package kr.cms.authService.service;

import kr.cms.authService.logging.LogProducer;
import kr.cms.common.dto.ApiResponse;
import kr.cms.authService.dto.TokenRequest;
import kr.cms.authService.dto.UserRequest;
import kr.cms.authService.exception.InvalidTokenException;
import kr.cms.authService.jwt.JwtProvider;
import kr.cms.common.dto.AuditLogDto;
import kr.cms.common.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtProvider jwtProvider;
    private final RedisUserTokenService redisService;
    private final LogProducer logProducer;

    public ApiResponse<TokenResponse> issueToken(UserRequest request, String ip, String userAgent) {
        if (request.getLoginId() == null || request.getRole() == null) {
            logProducer.sendAuditLog(new AuditLogDto(
                    "ISSUE_FAIL",
                    null,
                    "필수 정보 누락",
                    ip,
                    userAgent,
                    LocalDateTime.now()
            ));
            throw new IllegalArgumentException("로그인 ID 또는 권한 정보가 누락되었습니다.");
        }
        String loginId = request.getLoginId();

        String accessToken = jwtProvider.generateAccessToken(loginId, request.getRole());
        String refreshToken = redisService.saveRefreshToken(loginId);
        sendSuccessAudit("ISSUE_SUCCESS", loginId, ip, userAgent);
        return ApiResponse.success(new TokenResponse(accessToken, refreshToken));
    }

    public ApiResponse<TokenResponse> refreshToken(TokenRequest request, String ip, String userAgent) {
        String accessToken = request.getAccessToken();
        String refreshToken = request.getRefreshToken();

        try {
            String loginId = validateAndHandleInvalidToken(accessToken, refreshToken, ip, userAgent);
            String role = jwtProvider.getRoleFromAccessToken(accessToken);
            String newAccessToken = jwtProvider.generateAccessToken(loginId, role);
            String newRefreshToken = redisService.saveRefreshToken(loginId);

            sendSuccessAudit("REFRESH_TOKEN_SUCCESS", loginId, ip, userAgent);
            return ApiResponse.success(new TokenResponse(newAccessToken, newRefreshToken));
        } catch (InvalidTokenException e) {
            return ApiResponse.fail(e.getMessage());
        }
    }

    public ApiResponse<String> revokeToken(TokenRequest request, String ip, String userAgent) {
        String accessToken = request.getAccessToken();
        String refreshToken = request.getRefreshToken();

        try {
            String loginId = validateAndHandleInvalidToken(accessToken, refreshToken, ip, userAgent);

            redisService.deleteRefreshToken(refreshToken);
            redisService.addToBlacklist(accessToken, jwtProvider.getExpiration(accessToken));

            sendSuccessAudit("REVOKE_TOKEN_SUCCESS", loginId, ip, userAgent);
            return ApiResponse.successWithMessage("토큰이 정상적으로 폐기되었습니다.");
        } catch (InvalidTokenException e) {
            redisService.addToBlacklist(accessToken, jwtProvider.getExpiration(accessToken));
            return ApiResponse.success(e.getMessage());
        }
    }

    private String validateAndHandleInvalidToken(String accessToken, String refreshToken, String ip, String userAgent) {
        if (accessToken == null || refreshToken == null) {
            sendFailAuditEmptyToken("TOKEN_EMPTY", ip, userAgent);
            throw new InvalidTokenException("토큰이 누락되었습니다.");
        }

        String loginId = redisService.getLoginIdFromRefreshToken(
                refreshToken,
                jwtProvider.extractLoginId(accessToken)
        );

        if (loginId == null) {
            sendFailAuditInvalidToken("INVALID_TOKEN", ip, userAgent);
            throw new InvalidTokenException("유효하지 않은 토큰입니다.");
        }

        return loginId;
    }

    private void sendSuccessAudit(String action, String loginId, String ip, String agent) {
        logProducer.sendAuditLog(new AuditLogDto(
                action,
                loginId,
                "정상 처리됨",
                ip,
                agent,
                LocalDateTime.now()
        ));
    }

    private void sendFailAuditInvalidToken(String action, String ip, String userAgent) {
        logProducer.sendAuditLog(new AuditLogDto(
                action,
                null,
                "유효하지 않은 토큰",
                ip,
                userAgent,
                LocalDateTime.now()
        ));
    }

    private void sendFailAuditEmptyToken(String action, String ip, String userAgent) {
        logProducer.sendAuditLog(new AuditLogDto(
                action,
                null,
                "토큰이 누락되었습니다.",
                ip,
                userAgent,
                LocalDateTime.now()
        ));
    }
}