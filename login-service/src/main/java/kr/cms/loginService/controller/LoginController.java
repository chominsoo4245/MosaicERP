package kr.cms.loginService.controller;

import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.TokenResponse;
import kr.cms.loginService.dto.LoginRequest;
import kr.cms.loginService.dto.TokenRequest;
import kr.cms.loginService.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login-service")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(@RequestBody LoginRequest request) {
        return loginService.login(request);
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestBody TokenRequest request) {
        System.out.println("request:" + request);
        return loginService.logout(request);
    }
}
