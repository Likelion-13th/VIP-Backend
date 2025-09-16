
//클래스가 속한 패키지 지정
package likelion13th.princess_edition.login.auth.repository;

import likelion13th.princess_edition.domain.User;
import likelion13th.princess_edition.login.auth.jwt.RefreshToken;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * RefreshToken 저장소
 * - 사용자(User)와 1:1로 매핑된 RefreshToken을 조회/삭제한다.
 * - Spring Data JPA의 파생 쿼리와 @Query(JPQL)를 혼용.
 */

//refreshtoken; 관리할 엔티티
//PK타입 = Long -> 보통 pk는 숫자로 자동 증가, 따라서 long으로 받음
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // 사용자 엔티티로 RefreshToken 한 건을 조회
    // - 존재하지 않을 수 있으므로 Optional로 감싼다.
    Optional<RefreshToken> findByUser(User user);

    // 사용자 기준으로 RefreshToken을 삭제 (JPQL 직접 정의)
    // - @Modifying: DML(DELETE/UPDATE) 쿼리임을 명시
    // - 트랜잭션 경계(@Transactional)는 서비스 레이어에서 감싸는 것을 권장
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.user = :user")
    void deleteByUser(@Param("user") User user);
}

/*
1) 왜 필요한가
- RefreshToken을 DB에서 관리함(저장/조회/삭제 등)

로그인을 시도할 때, TokenProvider이 AccessToken과 RefreshToken을 발급하면 DB에 저장함.

토큰 만료 시, 서버가 DB에서 해당 토큰을 찾음
유효할 경우 새 accesstoken발급

로그아웃을 시도할 때, DB에 있는 토큰 삭제


2) 없으면/틀리면?
-refreshtoken을 관리할 수 없음.
-조회/저장/삭제 불가능
-로그아웃을 했는데도 삭제 x면 보안상 문제 생길 수 있음
 */