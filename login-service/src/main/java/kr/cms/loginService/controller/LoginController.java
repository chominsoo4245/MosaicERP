package kr.cms.loginService.controller;

import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.TokenResponse;
import kr.cms.loginService.dto.LoginRequest;
import kr.cms.loginService.dto.TokenRequest;
import kr.cms.loginService.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login-service")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(@RequestBody LoginRequest request, @RequestHeader("X-Forwarded-For") String ip, @RequestHeader("X-User-Agent") String userAgent) {
        ip = ip.split(",")[0];

        return loginService.login(request, ip, userAgent);
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestBody TokenRequest request, @RequestHeader("X-Forwarded-For") String ip, @RequestHeader("X-User-Agent") String userAgent) {
        ip = ip.split(",")[0];

        return loginService.logout(request, ip, userAgent);
    }
}
