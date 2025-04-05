package kr.cms.authService.dto;

import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    private String accessToken;
    private String refreshToken;
}
