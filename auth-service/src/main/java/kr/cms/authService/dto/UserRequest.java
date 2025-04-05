package kr.cms.authService.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String loginId;
    private String role;
}