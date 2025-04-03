package kr.cms.authService.controller;

import kr.cms.authService.dto.LoginRequest;
import kr.cms.authService.dto.LogoutRequest;
import kr.cms.authService.dto.RefreshTokenRequest;
import kr.cms.authService.dto.TokenResponse;
import kr.cms.authService.jwt.JwtProvider;
import kr.cms.authService.repository.UserRepository;
import kr.cms.authService.security.CustomUserDetails;
import kr.cms.authService.service.RedisUserTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RedisUserTokenService rutService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getPassword())
        );
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // user data (loginId, role)
        String loginId =  userDetails.getUsername();
        String userRole =  userDetails.getRole();

        // token (access, refresh)
        String accessToken = jwtProvider.generateAccessToken(loginId, userRole);
        String refreshToken = rutService.saveRefreshToken(loginId);

        return new TokenResponse(accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody RefreshTokenRequest request){
        String uuid = request.getRefreshToken();
        String username = rutService.getUsernameFromRefreshToken(uuid);

        if (username == null) {
            throw new RuntimeException("Invalid refresh token");
        }

        String role = userRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getUserRole().getName();

        String newAccessToken = jwtProvider.generateAccessToken(username, role);
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
