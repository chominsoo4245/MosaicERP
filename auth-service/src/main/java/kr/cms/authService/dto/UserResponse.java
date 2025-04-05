package kr.cms.authService.dto;

import lombok.Getter;

@Getter
public class UserResponse {
    private String loginId;
    private String password;
    private String role;
}