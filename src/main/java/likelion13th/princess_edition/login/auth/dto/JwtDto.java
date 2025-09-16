package likelion13th.princess_edition.login.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//Lombok 어노테이션을 사용하기 위해 불러옴 (Getter, Setter, Equals, Builder, ToString 등)

@Getter
@Setter
@ToString
@Builder

//클래스 선언
public class JwtDto {
    private String accessToken;
    private String refreshToken;

    public JwtDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}

/*
1) 왜 필요한가
- 클라이언트 <-> 서버  JWT 데이터를 주고받을 때 사용
- 로그인 성공 시, accessToken과 refreshToken 생성 및 전달
- 객체 생성 후 반환 -> 유지 보수 용이

2) 없으면/틀리면?
- 토큰을 주고 받는 방법에 변화가 생김 ( MAP 이나 문자열 사용 )
- 코드가 난잡해지고(구조 지저분, 유지 보수 어렵) 실수가 생길 수 있다.
*/