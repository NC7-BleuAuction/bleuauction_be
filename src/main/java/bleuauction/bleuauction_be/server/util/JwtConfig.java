package bleuauction.bleuauction_be.server.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expriration-tiem}")
  private int exprirationTiem;

  @Value("${jwt.token-prefix}")
  private String toekenPrefix;

//  @Value("${jwt.header-string}")
//  private String headerString;

  public String getSecret() {
    return secret;
  }

  public int getExprirationTiem() {
    return exprirationTiem;
  }

  public String getToekenPrefix() {
    return toekenPrefix;
  }

//  public String getHeaderString() {
//    return headerString;
//  }
}