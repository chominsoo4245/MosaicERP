package kr.cms.authService.repository;

import kr.cms.authService.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"userRole", "userStatus"})
    Optional<User> findByLoginId(String loginId);
}
