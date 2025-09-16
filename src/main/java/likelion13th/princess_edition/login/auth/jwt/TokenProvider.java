package likelion13th.princess_edition.login.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import likelion13th.princess_edition.login.auth.dto.JwtDto;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.parsers.ReturnTypeParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
    private final Key secretkey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final ReturnTypeParser returnTypeParser;

    public TokenProvider(
            @Value("${JWT_SECRET") String secretkey,
            @Value("${JWT_EXPIRATION") long accessTokenExpiration,
            @Value("${JWT_REFRESH_EXPIRATION") long refreshTokenExpiration, ReturnTypeParser returnTypeParser)
    {
        this.secretkey = Keys.hmacShaKeyFor(secretkey.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.returnTypeParser = returnTypeParser;
    }

    public JwtDto generateToken(UserDetails userDetails) {
        log.info("JWT 생성: 사용자 {}", userDetails.getUsername());

        String userId = userDetails.getUsername();

        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = createToken(userId, authorities, accessTokenExpiration);

        String refreshToken = createToken(userId, null, refreshTokenExpiration);

        log.info("JWT 생성 완료: 사용자 {}", userDetails.getUsername());
        return new JwtDto(accessToken, refreshToken);
    }

    private String createToken(String providerId, String authorities, long expirationTime) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(providerId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretkey, SignatureAlgorithm.HS256);

        if (authorities != null) {
            builder.claim("authorities", authorities);
        }

        return builder.compact().toString();

    }
//토큰 검증(서명 검증+만료검증)
    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder()
                    .setSigningKey(secretkey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (JwtException e){
            return false;
        }
    }

    public Claims parseToken(String token) {
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(secretkey)
                    .build()
                    .parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            log.warn("토큰 만료");
            throw e;
        }catch (JwtException e){
            log.warn("JWT 파싱 실패");
            throw new RuntimeException(e);
        }
    }
//claims -> GrantedAuthority 변환
    public Collection<? extends GrantedAuthority> getAuthorities(Claims claims) {
        String authoritiesString = claims.get("authorities", String.class);
        if (authoritiesString != null || authoritiesString.isEmpty()) {
            log.warn("권한 정보가 없다 - 기본 ROLE_USER 부여");
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return Arrays.stream(authoritiesString.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Claims parseClaimsAllowExpired(String token) {
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(secretkey)
                    .build()
                    .parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            //만료됐어도 claim 가져올 수 있게 예외처리?
            return e.getClaims();
        }
    }
}

/*
1) 왜 필요한가
- JWT 생성(accessToken과 refreshToken) 및 유효성 검증


2) 없으면/틀리면?
-토큰 발급 불가(생성 불가)
-보안 및 위조 가능(검증 불가)
 */
