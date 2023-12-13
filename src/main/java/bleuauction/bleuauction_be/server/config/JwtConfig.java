package bleuauction.bleuauction_be.server.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class JwtConfig {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expriration-tiem}")
  private Long exprirationTiem;

  @Value("${jwt.token-prefix}")
  private String toekenPrefix;

  @Value("${jwt.refreshToken-reissueCriteriaSeconds}")
  private Long reissueCriteriaSeconds;

//  @Value("${jwt.header-string}")
//  private String headerString;

}