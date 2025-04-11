package kr.cms.loginService.controller;

import kr.cms.common.dto.ApiResponse;
import kr.cms.common.dto.HeaderInfoDTO;
import kr.cms.common.dto.TokenResponse;
import kr.cms.common.extractor.HeaderExtractor;
import kr.cms.common.provider.HeaderProvider;
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
    private final HeaderProvider headerProvider;

    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(
            @RequestBody LoginRequest request
    ) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return loginService.login(request, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent());
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(
            @RequestBody TokenRequest request
    ) {
        HeaderInfoDTO headerInfoDTO = HeaderExtractor.extractHeaders(headerProvider, HeaderInfoDTO.class);
        return loginService.logout(request, headerInfoDTO.getIp(), headerInfoDTO.getUserAgent(), headerInfoDTO.getLoginId());
    }
}
