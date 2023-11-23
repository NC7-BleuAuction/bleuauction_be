package bleuauction.bleuauction_be.server.common.jwt;

import bleuauction.bleuauction_be.server.exception.ExpiredTokenException;
import bleuauction.bleuauction_be.server.exception.InvalidTokenException;
import bleuauction.bleuauction_be.server.exception.RenewAccessTokenException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateJwt {

  private final JwtConfig jwtConfig;
  public static final String UNAUTHORIZED_ACCESS = "UA";

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

  public void verifyAccessToken(String authorizationHeader) throws InvalidTokenException {
    if (authorizationHeader == null || !authorizationHeader.startsWith(jwtConfig.getToekenPrefix())) {
      log.error("jwtConfig.getToekenPrefix()와 불일치! : {}", authorizationHeader);
      throw InvalidTokenException.EXCEPTION;
    }

    String pureTokenStr = authorizationHeader.replace(jwtConfig.getToekenPrefix(), "");
    isTokenExpired(pureTokenStr);
  }

  public void isTokenExpired(String pureTokenStr) throws ExpiredTokenException {
    log.info("pureTokenStr: {} ", pureTokenStr);
    DecodedJWT decodedJWT = JWT.decode(pureTokenStr);
    long currentTime = System.currentTimeMillis() / 1000;
    long exp = decodedJWT.getClaim("exp").asLong();
    if (exp < currentTime) {
      log.error("토큰의 유효기간이 만료되었습니다. ===========> exp: {},  currentTime: {}", exp, currentTime);
      throw ExpiredTokenException.EXCEPTION;
    }
  }

  public String getRenewAccessToken(String refreshTokenHeader) throws RenewAccessTokenException {
    log.info("refreshToken: {} ", refreshTokenHeader);
    try {
      verifyAccessToken(refreshTokenHeader);
    } catch (Exception e) {
      throw RenewAccessTokenException.EXCEPTION;
    }
    return createAccessToken(TokenMember.of(refreshTokenHeader));
  }

  public TokenMember getTokenMember(String authorizationHeader) {
    log.info("authorizationHeader: {}", authorizationHeader);
    verifyAccessToken(authorizationHeader);
    String pureTokenStr = authorizationHeader.replace(jwtConfig.getToekenPrefix(), "");
    return TokenMember.of(pureTokenStr);
  }
}