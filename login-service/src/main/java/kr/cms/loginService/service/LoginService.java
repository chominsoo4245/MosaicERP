package kr.cms.loginService.service;

import kr.cms.loginService.dto.LoginRequest;
import kr.cms.loginService.entity.User;
import kr.cms.loginService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import kr.cms.common.dto.TokenResponse;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        // Auth-service로 accessToken, refreshToken 요청
        String authUrl = "http://auth-service/auth/login";
        return restTemplate.postForObject(authUrl, request, TokenResponse.class);
    }
}