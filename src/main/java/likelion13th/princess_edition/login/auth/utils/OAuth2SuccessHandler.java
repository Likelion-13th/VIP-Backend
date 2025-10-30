package likelion13th.princess_edition.login.auth.utils;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion13th.princess_edition.domain.Address;
import likelion13th.princess_edition.domain.User;
import likelion13th.princess_edition.login.auth.jwt.CustomUserDetails;
import likelion13th.princess_edition.login.auth.service.JpaUserDetailsManager;
import likelion13th.princess_edition.login.auth.dto.JwtDto;
import likelion13th.princess_edition.login.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.security.core.Authentication;
import java.util.List;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JpaUserDetailsManager jpaUserDetailsManager;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        String providerId = (String) oAuth2User.getAttribute("provider_id");
        String nickname   = (String) oAuth2User.getAttribute("nickname");

        String maskedPid  = (providerId != null && providerId.length() > 4) ? providerId.substring(0, 4) + "***" : "***";
        String maskedNick = (nickname != null && !nickname.isBlank()) ? "*(hidden)*" : "(none)";
        log.info("OAuth2 Success - providerId(masked)={}, nickname={}", maskedPid, maskedNick);

        if (!jpaUserDetailsManager.userExists(providerId)) {
            User newUser = User.builder()
                    .providerId(providerId)
                    .usernickname(nickname)
                    .deletable(true)
                    .build();

            newUser.setAddress(new Address("10540", "경기도 고양시 덕양구 항공대학로 76", "한국항공대학교"));

            CustomUserDetails userDetails = new CustomUserDetails(newUser);
            jpaUserDetailsManager.createUser(userDetails);
            log.info("신규 회원 등록 완료 - providerId(masked)={}", maskedPid);
        } else {
            log.info("기존 회원 로그인 - providerId(masked)={}", maskedPid);
        }

        JwtDto jwt = userService.jwtMakeSave(providerId);
        log.info("JWT 발급 완료 - providerId(masked)={}", maskedPid);

        String frontendRedirectUri = request.getParameter("redirect_uri");
        List<String> authorizedUris = List.of(
                "https://vip-likelion.netlify.app",
                "http://localhost:3000"
        );
        if (frontendRedirectUri == null || !authorizedUris.contains(frontendRedirectUri)) {
            frontendRedirectUri = "https://vip-likelion.netlify.app";
        }

        String redirectUrl = UriComponentsBuilder
                .fromUriString(frontendRedirectUri)
                .queryParam("accessToken", jwt.getAccessToken())
                .build()
                .toUriString();

        log.info("Redirecting to authorized frontend host: {}", frontendRedirectUri);

        response.sendRedirect(redirectUrl);
    }
}

/*
1) 왜 필요한가
- OAuth2 인증 성공 후 사용자 정보 가져옴
-신규 사용자와 기존 사용자 분류

로그인 성공 이후 회원가입/로그인/JWT발급 등을 담당함


2) 없으면/틀리면?
- 로그인 이후 회원 정보가 DB에 저장되지 않기 때문에 신규 사용자가 로그인을 할 수 없다.
- JWT가 발급x
 */