package bleuauction.bleuauction_be.server.config.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class TokenProvider {

  private final JwtProperties jwtProperties;

  public String generateToken(User user, Duration expiredAt) {
    Date now = new Date();
    return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
  }

  // 토큰 생성
  private String makeToken(Date expiry, User user) {
    Date now = new Date();

    return Jwts.builder()
  }

}
