package kr.cms.authService.controller;

import kr.cms.authService.dto.*;
import kr.cms.authService.jwt.JwtProvider;
import kr.cms.authService.service.RedisUserTokenService;
import kr.cms.common.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final JwtProvider jwtProvider;
    private final RedisUserTokenService rutService;

    @PostMapping("/issue")
    public TokenResponse login(@RequestBody LoginRequest loginRequest) {
        String accessToken = jwtProvider.generateAccessToken(loginRequest.getLoginId(), loginRequest.getRole());
        String refreshToken = rutService.saveRefreshToken(loginRequest.getLoginId());

        return new TokenResponse(accessToken, refreshToken);
    }
    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody RefreshTokenRequest request) {
        String oldRefreshToken = request.getRefreshToken();
        String accessToken = request.getAccessToken();

        String loginId = rutService.getLoginIdFromRefreshToken(oldRefreshToken);
        if (loginId == null) {
            throw new RuntimeException("Invalid refresh token");
        }

        rutService.deleteRefreshToken(oldRefreshToken);
        String role = jwtProvider.getRoleFromAccessToken(accessToken);

        String newAccessToken = jwtProvider.generateAccessToken(loginId, role);
        String newRefreshToken = rutService.saveRefreshToken(loginId);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    @PostMapping("/revoke")
    public void logout(@RequestBody LogoutRequest logoutRequest) {
        String refreshUuid = logoutRequest.getRefreshToken();
        String accessToken = logoutRequest.getAccessToken();

        rutService.deleteRefreshToken(refreshUuid);
        long expiration = jwtProvider.getExpiration(accessToken);
        rutService.addToBlacklist(accessToken, expiration);
    }
}
