package com.juny.finalboard.global.security.user.jwt;

import com.juny.finalboard.global.constant.Constants;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtUtil {

  private final Long accessTokenExpiredMileSecond;
  private final SecretKey secretKey;

  public JwtUtil(
      @Value("${jwt.secret-key}") String secret,
      @Value("${jwt.access-token-expired-mile-second}") Long expiredMs) {
    secretKey =
        new SecretKeySpec(
            secret.getBytes(StandardCharsets.UTF_8), SIG.HS256.key().build().getAlgorithm());
    accessTokenExpiredMileSecond = expiredMs;
  }

  /**
   *
   *
   * <h1>액세스 토큰 만료 밀리 초</h1>
   *
   * @return Long
   */
  public Long getAccessTokenExpiredMileSecond() {

    return accessTokenExpiredMileSecond;
  }

  /**
   *
   *
   * <h1>Jwt 토큰에서 아이디 조회 </h1>
   *
   * @param token AccessToken
   * @return id
   */
  public Long getId(String token) {

    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get(Constants.ID, Long.class);
  }

  /**
   *
   *
   * <h1>Jwt 토큰에서 권한 조회 </h1>
   *
   * @param token AccessToken
   * @return role
   */
  public String getRole(String token) {

    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get(Constants.ROLE, String.class);
  }

  public String getName(String token) {

    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get(Constants.NAME, String.class);
  }

  /**
   *
   *
   * <h1>Jwt 토큰에서 만료기간 조회 </h1>
   *
   * @param token AccessToken
   * @return expiredDate
   */
  public Date getExpiration(String token) {

    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getExpiration();
  }

  public String createJwt(Long id, String role, String name, Date expired) {

    return Jwts.builder()
        .claim(Constants.ID, id)
        .claim(Constants.ROLE, role)
        .claim(Constants.NAME, name)
        .issuedAt(new Date())
        .expiration(expired)
        .signWith(secretKey)
        .compact();
  }

  /**
   *
   *
   * <h1>Jwt 토큰에서 유효성 검사 </h1>
   *
   * @param token AccessToken
   * @return 1 - "AT1" 액세스 토큰 유효기간 만료, 2 - "AT2" 유효하지 않은 액세스 토큰
   */
  public int isInValid(String token) {

    try {
      Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
    } catch (Exception e) {
      if (e instanceof ExpiredJwtException) {
        log.error(e.getMessage());
        return 1;
      } else {
        log.error(e.getMessage());
        return 2;
      }
    }

    return 0;
  }
}
