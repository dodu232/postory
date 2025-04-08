package org.example.postory.domain.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.example.postory.domain.auth.dto.JwtToken;
import org.example.postory.domain.user.service.UserService;
import org.example.postory.global.exception.ApiException;
import org.example.postory.global.exception.ErrorType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProvider {

    private final Key key;
    private final UserService userService;

    /**
     * 생성자를 통한 JWT 서명용 Key 초기화 application.property에서 secret 값 가져와서 key에 저장
     */
    public JwtProvider(@Value("${jwt.secret}") String secretKey, UserService userService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userService = userService;
    }

    /**
     * AccessToken과 RefreshToken을 생성하는 메서드
     */
    public JwtToken generateToken(Long userId) {
        long now = (new Date()).getTime();
        Date issuedAt = new Date();

        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");

        // Access Token 생성
        String accessToken = Jwts.builder()
            .setSubject(String.valueOf(userId))
            .claim("auth", "ROLE_USER")
            .claim("iss", "off")
            .setExpiration(new Date(now + 1800000))
            .setIssuedAt(issuedAt)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
            .claim("iss", "off")
            .claim("add", "ref")
            .setExpiration(new Date(now + 604800000))
            .setIssuedAt(issuedAt)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
        userService.saveToken(userId, refreshToken);

        return JwtToken.builder()
            .grantType("Bearer")
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    /**
     * JWT를 복호화하여 토큰에 들어있는 정보를 꺼내 Authentication 객체를 생성하는 메서드
     */
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        System.out.println("토큰 " + token);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(
                claims.get("auth").toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * 토큰 유효성을 검증하는 메서드
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.TOKEN_ERROR, "토큰이 잘못되었습니다.");
        } catch (ExpiredJwtException e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, ErrorType.TOKEN_ERROR, "토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, ErrorType.TOKEN_ERROR,
                "지원하지 않는 토큰입니다.");
        } catch (Exception e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, ErrorType.TOKEN_ERROR, "토큰 유효성 에러입니다.");
        }
    }

    /**
     * RefreshToken을 이용하여 AccessToken을 재발급하는 메서드
     */
    public JwtToken refreshToken(String refreshToken) {
        try {
            Authentication authentication = getAuthentication(refreshToken);
            long id = 1;
            String getRefreshToken = userService.getRefreshToken(id);

            JwtToken refreshGetToken = null;

            if (refreshToken.equals(getRefreshToken)) {
                refreshGetToken = generateToken(id);

                userService.saveToken(id, refreshGetToken.getRefreshToken());
                return refreshGetToken;
            } else {
                log.warn("does not exist Token");
                throw new ApiException(HttpStatus.UNAUTHORIZED, ErrorType.TOKEN_ERROR, "에러에러");
            }
        } catch (NullPointerException e) {
            log.warn("does not exist Token");
            throw new ApiException(HttpStatus.UNAUTHORIZED, ErrorType.TOKEN_ERROR,
                "refreshToken없음");
        } catch (NoSuchElementException e) {
            log.warn("no such Token value");
            throw new ApiException(HttpStatus.UNAUTHORIZED, ErrorType.TOKEN_ERROR,
                "db에 refreshToken없음");
        }
    }

    /**
     * JWT 토큰을 파싱하여 클레임 정보를 반환하는 메서드
     */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
