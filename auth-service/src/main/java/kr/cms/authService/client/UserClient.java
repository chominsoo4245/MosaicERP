package kr.cms.authService.client;

import kr.cms.authService.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", fallback = UserClientFallback.class)
public interface UserClient {
    @GetMapping("/internal/users/loginId/{loginId}")
    UserResponse getUserByLoginId(@PathVariable("loginId") String loginId);
}