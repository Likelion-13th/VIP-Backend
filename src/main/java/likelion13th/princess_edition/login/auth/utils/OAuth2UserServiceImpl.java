package likelion13th.princess_edition.login.auth.utils;

import likelion13th.princess_edition.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String providerId = oAuth2User.getAttributes().get("id").toString();

        @SuppressWarnings("unhecked")
        Map<String , Object> properties =
                (Map<String , Object>) oAuth2User.getAttributes().getOrDefault("properties", Collections.emptyMap());
        String nickname = properties.getOrDefault("nickname", "카카오사용자").toString();

        Map<String, Object> extendedAttributes = new HashMap<>(oAuth2User.getAttributes());
        extendedAttributes.put("provider_id", providerId);
        extendedAttributes.put("nickname", nickname);

        return new DefaultOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                extendedAttributes,
                "provider_id"
        );

    }
}

/*
1) 왜 필요한가
- OAuth2인증 서버에서 주는 응답을 받아옴
- provider_id나 nickname 등을 변환하여 spring security 형식으로 반환
 -->회원가입/로그인/JWT발급


2) 없으면/틀리면?
- 카카오가 주는 고유 ID를 못 가져와서 사용자를 구분하기 힘들다 --> 회원 중복
- 회원의 정보를 받아올 수 없음
-provide_id 등의 정보를 읽을 수 없음 --> DB에 저장하거나 JWT를 발급받기 힘듦
 */