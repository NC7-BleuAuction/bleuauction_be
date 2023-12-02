package bleuauction.bleuauction_be.server.common.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class JwtConfig {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expriration-tiem}")
  private int exprirationTiem;

  @Value("${jwt.token-prefix}")
  private String toekenPrefix;

//  @Value("${jwt.header-string}")
//  private String headerString;

}