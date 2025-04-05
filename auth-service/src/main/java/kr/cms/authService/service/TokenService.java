package kr.cms.authService.service;

import kr.cms.authService.dto.ApiResponse;
import kr.cms.authService.dto.TokenRequest;
import kr.cms.authService.dto.UserRequest;
import kr.cms.authService.exception.InvalidTokenException;
import kr.cms.authService.jwt.JwtProvider;
import kr.cms.common.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtProvider jwtProvider;
    private final RedisUserTokenService redisService;

    public ApiResponse<TokenResponse> issueToken(UserRequest request) {
        if (request.getLoginId() == null || request.getRole() == null) {
            throw new IllegalArgumentException("로그인 ID 또는 권한 정보가 누락되었습니다.");
        }

        String accessToken = jwtProvider.generateAccessToken(request.getLoginId(), request.getRole());
        String refreshToken = redisService.saveRefreshToken(request.getLoginId());
        return ApiResponse.success(new TokenResponse(accessToken, refreshToken));
    }

    public ApiResponse<TokenResponse> refreshToken(TokenRequest request) {
        String accessToken = request.getAccessToken();
        String refreshToken = request.getRefreshToken();

        String loginId = validateAndHandleInvalidToken(accessToken, refreshToken);
        String role = jwtProvider.getRoleFromAccessToken(accessToken);
        String newAccessToken = jwtProvider.generateAccessToken(loginId, role);
        String newRefreshToken = redisService.saveRefreshToken(loginId);

        return ApiResponse.success(new TokenResponse(newAccessToken, newRefreshToken));
    }

    public ApiResponse<String> revokeToken(TokenRequest request) {
        String accessToken = request.getAccessToken();
        String refreshToken = request.getRefreshToken();

        validateAndHandleInvalidToken(accessToken, refreshToken);
        redisService.deleteRefreshToken(refreshToken);
        redisService.addToBlacklist(accessToken, jwtProvider.getExpiration(accessToken));

        return ApiResponse.success("토큰이 정상적으로 폐기되었습니다.");
    }

    private String validateAndHandleInvalidToken(String accessToken, String refreshToken) {
        String loginId = redisService.getLoginIdFromRefreshToken(
                refreshToken,
                jwtProvider.extractLoginId(accessToken)
        );

        // accessToken 탈취 or refreshToken 방지
        if (loginId == null) {
            redisService.addToBlacklist(accessToken, jwtProvider.getExpiration(accessToken));
            throw new InvalidTokenException("유효하지 않은 토큰입니다. 토큰이 폐기되었습니다.");
        }

        return loginId;
    }
}