package kr.cms.authService.client;

import kr.cms.authService.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {
    @Override
    public UserResponse getUserByLoginId(String loginId) {
        throw new RuntimeException("user-service가 현재 사용 불가능합니다.");
    }
}