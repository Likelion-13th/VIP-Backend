package likelion13th.princess_edition.login.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import likelion13th.princess_edition.domain.User;

import likelion13th.princess_edition.global.api.ErrorCode;
import likelion13th.princess_edition.global.exception.GeneralException;
import likelion13th.princess_edition.login.auth.dto.JwtDto;
import likelion13th.princess_edition.login.auth.jwt.RefreshToken;
import likelion13th.princess_edition.login.auth.jwt.TokenProvider;
import likelion13th.princess_edition.login.auth.repository.RefreshTokenRepository;
import likelion13th.princess_edition.login.auth.service.JpaUserDetailsManager;
import likelion13th.princess_edition.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service

public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final JpaUserDetailsManager manager;

    public Boolean checkMemberByProviderId(String providerId) {
        return userRepository.existsByProviderId(providerId);

    }

    public Optional<User> findByProvideId(String providerId){
        return userRepository.findByProviderId(providerId);

    }

    public User getAuthenticatedUser(String providerId){
        return userRepository.findByProviderId(providerId)
                .orElseThrow(()->new GeneralException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void saveRefreshToken(String providerId, String refreshToken) {
    User user = userRepository.findByProviderId(providerId)
            .orElseThrow(()-> new GeneralException(ErrorCode.USER_NOT_FOUND));

    RefreshToken token = refreshTokenRepository.findByUser(user)
            .map(existingToken ->{
                existingToken.updateRefreshToken(refreshToken);
                return existingToken;
            })
            .orElseGet(()->{
                log.info("새 RefreshToken 생성 시도 (user_id={})", user.getId());
                return RefreshToken.builder()
                        .user(user)
                        .refreshToken(refreshToken)
                        .ttl(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)
                        .build();
            });

            refreshTokenRepository.save(token);
            log.info("RefreshToken 저장 완료 (user_id={})", user.getId());
    }

    @Transactional
    public JwtDto jwtMakeSave(String providerId){
        UserDetails details = manager.loadUserByUsername(providerId);

        JwtDto jwtDto = tokenProvider.generateToken(details);

        saveRefreshToken(providerId, jwtDto.getRefreshToken());
        return jwtDto;

    }

    @Transactional
    public JwtDto reissue(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }

        Claims claims;
        try {
            claims = tokenProvider.parseClaimsAllowExpired(accessToken);
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.TOKEN_INVALID);
        }
        String providerId = claims.getSubject();

        if (providerId == null || providerId.isEmpty()) {
            throw new GeneralException(ErrorCode.TOKEN_INVALID);
        }

        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> {
                    return new GeneralException(ErrorCode.USER_NOT_FOUND);
                });

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new GeneralException(ErrorCode.WRONG_REFRESH_TOKEN));

        if (!tokenProvider.validateToken(refreshToken.getRefreshToken())) {
            refreshTokenRepository.deleteByUser(user);
            throw new GeneralException(ErrorCode.TOKEN_EXPIRED);
        }

        UserDetails userDetails = manager.loadUserByUsername(providerId);
        JwtDto newJwt = tokenProvider.generateToken(userDetails);

        refreshToken.updateRefreshToken(newJwt.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        return newJwt;
    }

    @Transactional
    public void logout(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);

        }

        Claims claims = tokenProvider.parseToken(accessToken);
        String providerId = claims.getSubject();
        if (providerId == null || providerId.isEmpty()) {
            throw new GeneralException(ErrorCode.TOKEN_INVALID);
        }

        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.flush();
    }
}