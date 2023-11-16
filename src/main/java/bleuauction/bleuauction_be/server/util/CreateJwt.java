package bleuauction.bleuauction_be.server.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateJwt {
  public static final String SERVER_ERROR = "SE";
  public static final String REFRESH_ACCESS_TOKEN_ERROR = "RATE";
  public static final String VALID_TOKEN = "V";
  public static final String EXPIRED_TOKEN = "E";
  public static final String INVALID_TOKEN = "I";
  public static final String UNAUTHORIZED_ACCESS = "UA";


  private final JwtConfig jwtConfig;

  public String createAccessToken(TokenMember tokenMember) {

    return JWT.create()
            .withSubject(tokenMember.getMemberNo() + "")
            .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getExprirationTiem()))
            .withClaim("memberEmail", tokenMember.getMemberEmail())
            .withClaim("memberName", tokenMember.getMemberName())
            .withClaim("memberCategory", tokenMember.getMemberCategory())
            .sign(Algorithm.HMAC256(jwtConfig.getSecret()));
  }

  public String createRefreshToken(TokenMember tokenMember, String accessToken) {
    return JWT.create()
            .withSubject(tokenMember.getMemberNo() + "")
            .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getExprirationTiem() * 2L))
            .withClaim("accessToken", accessToken)
            .withClaim("memberEmail", tokenMember.getMemberEmail())
            .withClaim("memberName", tokenMember.getMemberName())
            .withClaim("memberCategory", tokenMember.getMemberCategory())
            .sign(Algorithm.HMAC256(jwtConfig.getSecret()));
  }

  public ResponseEntity<?> verifyAccessToken(String authorizationHeader, CreateJwt createJwt) {
    if (authorizationHeader == null || !authorizationHeader.startsWith(jwtConfig.getToekenPrefix())) {
      log.error("jwtConfig.getToekenPrefix()와 불일치! : " + authorizationHeader);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CreateJwt.INVALID_TOKEN);
    }

    String token = authorizationHeader.replace(jwtConfig.getToekenPrefix(), "");
    String validCheckStr = createJwt.isTokenValid(token);

    if (CreateJwt.EXPIRED_TOKEN.equals(validCheckStr) || CreateJwt.INVALID_TOKEN.equals(validCheckStr)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(validCheckStr);
    }

    return null;
  }

  public String isTokenValid(String jwtToken) {
    log.info("isAccessTokenValid(): ");
    log.info("jwtToken: " + jwtToken);

    try {
      DecodedJWT decodedJWT = JWT.decode(jwtToken);

      long currentTime = System.currentTimeMillis() / 1000;
      long exp = decodedJWT.getClaim("exp").asLong();
      if (exp < currentTime) {
        log.error("JWT의 유효기간이 만료되었습니다.");
        return EXPIRED_TOKEN;
      } else {
        log.info("JWT가 유효합니다.");
        return VALID_TOKEN;
      }
    } catch (JWTVerificationException e) {
      log.error("잘못된 JWT 토큰 입니다: " + e.getMessage());
      log.error("jwtToken: ", jwtToken);
      return INVALID_TOKEN;
    }
  }

  public String getRenewAccessToken(String refreshToken) {
    log.info("isRefreshTokenValid(): ");
    log.info("refreshToken: " + refreshToken);

    try {
      if (EXPIRED_TOKEN.equals(isTokenValid(refreshToken))) {
        return createAccessToken(TokenMember.of(refreshToken));
      }
    } catch (Exception e) {
      log.error("accessToken 재발급 중 에러 발생! " + e.getMessage());
    }
    return null;
  }

  public TokenMember getTokenMember(String authorizationHeader) {
    log.info("getTokenMember(): ");
    log.info("authorizationHeader: " + authorizationHeader);
    try {
      if (authorizationHeader == null || !authorizationHeader.startsWith(jwtConfig.getToekenPrefix())) {
        log.error("jwtConfig.getToekenPrefix()와 불일치! : " + authorizationHeader);
        return null;
      }
      String token = authorizationHeader.replace(jwtConfig.getToekenPrefix(), "");
      if(VALID_TOKEN.equals(isTokenValid(token))) {
        return TokenMember.of(token);
      }
    } catch (Exception e) {
      log.error("accessToken 디코딩 중 에러 발생! " + e.getMessage());
    }
    return null;
  }
}