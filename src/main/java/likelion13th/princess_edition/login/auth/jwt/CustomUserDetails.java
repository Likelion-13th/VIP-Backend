package likelion13th.princess_edition.login.auth.jwt;

import likelion13th.princess_edition.domain.User;
import likelion13th.princess_edition.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {

    //사용자 고유한 정보
    private Long userId;
    private String providerId;
    private String usernickname;
    private Address address;

    private Collection<? extends GrantedAuthority> authorities;

    /*user -> CustomUserDetails 변환
    DB에서 user 객체를 꺼내서 security가 알아듣도록 변환

    *만약 없을 경우, DB에서 꺼낸 사용자를 security에게 넘기지 못함 => 로그인 X
     */
    public CustomUserDetails(User user) {
        this.userId = user.getId();
        this.providerId = user.getProviderId();
        this.usernickname = user.getUsernickname();
        this.address = user.getAddress();
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    //
    public CustomUserDetails(String providerId, String password, Collection<? extends GrantedAuthority> authorities) {
        this.providerId = providerId;
        this.userId = null;
        this.usernickname = null;
        this.authorities = authorities;
        this.address = null;
    }

    public static CustomUserDetails fromEntity(User entity) {
        return CustomUserDetails.builder()
                .userId(entity.getId())
                .providerId(entity.getProviderId())
                .usernickname(entity.getUsernickname())
                .address(entity.getAddress())
                .build();
    }
// Security -> User 로 변환
//DB에 저장 혹은 활용 가능
    public User toEntity() {
        return User.builder()
                .id(this.userId)
                .providerId(this.providerId)
                .usernickname(this.usernickname)
                .address(this.address)
                .build();
    }
//재정의
    @Override
    public String getUsername() {
        return this.providerId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.authorities != null && !this.authorities.isEmpty()) {
            return this.authorities;
        }
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        // 소셜 로그인은 비밀번호를 사용하지 않음
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료 정책 사용 시 실제 값으로 교체
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 잠금 정책 사용 시 실제 값으로 교체
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 자격 증명(비밀번호) 만료 정책 사용 시 실제 값으로 교체
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 활성/비활성 정책 사용 시 실제 값으로 교체 (예: 탈퇴/정지 사용자)
        return true;
    }



}

/*
1) 왜 필요한가
- User(DB) <-> security (직접적으로 이해하기 힘듦)
-비밀번호가 필요하지 않은 특수 상황까지 포함 (소셜로그인)
-사용자 정보, 상태 확인가능


2) 없으면/틀리면?
-로그인 불가
-권한 관리 힘듦
-식별에 문제가 생기기 때문에 보안 취약으로도 이어질 수 있다?
*/