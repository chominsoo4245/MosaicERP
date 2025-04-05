package kr.cms.authService.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kr.cms.authService.exception.InvalidTokenException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    private final String secretKey = "secretsecretsecretsecretsecretsecret";
    private final long accessExpirationMs = 1000L * 60 * 15; // 15분

    public String generateAccessToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpirationMs))
                .signWith(getKey())
                .compact();
    }

    public String extractLoginId(String token)  throws InvalidTokenException{
        return getClaims(token).getSubject();
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new InvalidTokenException("유효하지 않은 JWT 토큰입니다.", e);
        }
    }

    public long getExpiration(String token) throws InvalidTokenException {
        Claims claims = getClaims(token);

        return claims.getExpiration().getTime();
    }

    public String getRoleFromAccessToken(String token)  throws InvalidTokenException{
        Claims claims = getClaims(token);

        return claims.get("role", String.class);
    }
}