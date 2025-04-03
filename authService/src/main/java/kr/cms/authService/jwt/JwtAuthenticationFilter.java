package kr.cms.authService.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.cms.authService.domain.UserStatus;
import kr.cms.authService.security.CustomUserDetails;
import kr.cms.authService.service.CustomUserDetailsService;
import kr.cms.authService.service.RedisUserTokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;
    private final RedisUserTokenService redisUserTokenService;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, CustomUserDetailsService userDetailsService, RedisUserTokenService redisUserTokenService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.redisUserTokenService = redisUserTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtProvider.validateToken(token) || redisUserTokenService.isBlacklisted(token)) {
                String username = jwtProvider.extractUsername(token);
                CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
                if(userDetails.getUser().getUserStatus().getStatusCode().equals("ACTIVE")) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        chain.doFilter(request, response);
    }
}
