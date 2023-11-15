package bleuauction.bleuauction_be.server.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
public class JwtProperties {
  @Value("{jwt.issuer}")
  private String issuer;

  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${jwt.expriration-tiem}")
  private int exprirationTiem;

  @Value("${jwt.token-prefix}")
  private String toekenPrefix;
}