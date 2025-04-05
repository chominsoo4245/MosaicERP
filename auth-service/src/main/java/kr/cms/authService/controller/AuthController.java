package kr.cms.authService.controller;

import kr.cms.authService.client.UserClient;
import kr.cms.authService.dto.*;
import kr.cms.authService.jwt.JwtProvider;
import kr.cms.authService.service.RedisUserTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final JwtProvider jwtProvider;
    private final UserClient userClient;
    private final RedisUserTokenService rutService;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest loginRequest) {
        UserResponse user = userClient.getUserByLoginId(loginRequest.getLoginId());

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtProvider.generateAccessToken(user.getLoginId(), user.getRole());
        String refreshToken = rutService.saveRefreshToken(user.getLoginId());

        return new TokenResponse(accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody RefreshTokenRequest request) {
        String uuid = request.getRefreshToken();
        String username = rutService.getUsernameFromRefreshToken(uuid);

        if (username == null) {
            throw new RuntimeException("Invalid refresh token");
        }

        UserResponse user = userClient.getUserByLoginId(username);
        String newAccessToken = jwtProvider.generateAccessToken(user.getLoginId(), user.getRole());

        return new TokenResponse(newAccessToken, uuid);
    }

    @PostMapping("/logout")
    public void logout(@RequestBody LogoutRequest logoutRequest) {
        String refreshUuid = logoutRequest.getRefreshToken();
        String accessToken = logoutRequest.getAccessToken();

        rutService.deleteRefreshToken(refreshUuid);
        long expiration = jwtProvider.getExpiration(accessToken);
        rutService.addToBlacklist(accessToken, expiration);
    }
}
