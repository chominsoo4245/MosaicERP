package kr.cms.authService;

import kr.cms.authService.domain.User;
import kr.cms.authService.domain.UserStatus;
import kr.cms.authService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestUserInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("admin").isEmpty()) {
            userRepository.save(
                    User.builder()
                            .loginId("admin")
                            .password(passwordEncoder.encode("1234"))
                            .role("ROLE_ADMIN")
                            .userStatus(UserStatus.ACTIVE)
                            .build()
            );
        }
    }
}
