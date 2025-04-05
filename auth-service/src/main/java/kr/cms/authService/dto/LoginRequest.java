package kr.cms.authService.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String loginId;
    private String role;
}