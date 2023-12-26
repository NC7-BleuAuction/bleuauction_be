package bleuauction.bleuauction_be.server.common.utils;


import bleuauction.bleuauction_be.server.common.exception.ExpiredTokenException;
import bleuauction.bleuauction_be.server.common.exception.InvalidTokenException;
import bleuauction.bleuauction_be.server.common.exception.RenewAccessTokenException;
import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import bleuauction.bleuauction_be.server.config.JwtConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtConfig jwtConfig;
    public static final String UNAUTHORIZED_ACCESS = "UA";

    public String createAccessToken(TokenMember tokenMember) {
        return JWT.create()
                .withSubject(tokenMember.getMemberNo() + "")
                .withExpiresAt(
                        new Date(System.currentTimeMillis() + jwtConfig.getExprirationTiem()))
                .withClaim("memberEmail", tokenMember.getMemberEmail())
                .withClaim("memberName", tokenMember.getMemberName())
                .withClaim("memberCategory", tokenMember.getMemberCategory().name())
                .sign(Algorithm.HMAC256(jwtConfig.getSecret()));
    }

    public String createRefreshToken(TokenMember tokenMember, String accessToken) {
        return JWT.create()
                .withSubject(tokenMember.getMemberNo() + "")
                .withExpiresAt(
                        new Date(System.currentTimeMillis() + jwtConfig.getExprirationTiem() * 2L))
                .withClaim("accessToken", accessToken)
                .withClaim("memberEmail", tokenMember.getMemberEmail())
                .withClaim("memberName", tokenMember.getMemberName())
                .withClaim("memberCategory", tokenMember.getMemberCategory().name())
                .sign(Algorithm.HMAC256(jwtConfig.getSecret()));
    }

    public void verifyToken(String authorizationHeader) throws InvalidTokenException {
        if (authorizationHeader == null
                || !authorizationHeader.startsWith(jwtConfig.getToekenPrefix())) {
            log.error("jwtConfig.getToekenPrefix()와 불일치! : {}", authorizationHeader);
            throw InvalidTokenException.EXCEPTION;
        }
        isTokenExpired(removedPrefixAuthHeader(authorizationHeader));
    }

    public void isTokenExpired(String pureTokenStr) throws ExpiredTokenException {
        log.info("pureTokenStr: {} ", pureTokenStr);
        DecodedJWT decodedJWT = JWT.decode(pureTokenStr);
        long currentTime = System.currentTimeMillis() / 1000;
        long exp = decodedJWT.getClaim("exp").asLong();
        if (exp < currentTime) {
            log.error(
                    "토큰의 유효기간이 만료되었습니다. ===========> exp: {},  currentTime: {}", exp, currentTime);
            throw ExpiredTokenException.EXCEPTION;
        }
    }

    public String getRenewAccessToken(String refreshTokenHeader) throws RenewAccessTokenException {
        log.info("refreshToken: {} ", refreshTokenHeader);
        try {
            verifyToken(refreshTokenHeader);
        } catch (Exception e) {
            throw RenewAccessTokenException.EXCEPTION;
        }
        return createAccessToken(TokenMember.of(removedPrefixAuthHeader(refreshTokenHeader)));
    }

    public TokenMember getTokenMember(String authorizationHeader) {
        log.info("authorizationHeader: {}", authorizationHeader);
        verifyToken(authorizationHeader);
        return TokenMember.of(removedPrefixAuthHeader(authorizationHeader));
    }

    public String removedPrefixAuthHeader(String authorizationHeader) {
        return authorizationHeader.replace(jwtConfig.getToekenPrefix(), "");
    }

    public boolean isReissuanceRefreshToken(String pureTokenStr) {
        DecodedJWT decodedJWT = JWT.decode(pureTokenStr);

        // 1초로 통일
        long currentTime = System.currentTimeMillis() / 1000;
        long exp = decodedJWT.getClaim("exp").asLong();
        long gapTime = currentTime - exp;

        return gapTime < jwtConfig.getReissueCriteriaSeconds() ? true : false;
    }
}
