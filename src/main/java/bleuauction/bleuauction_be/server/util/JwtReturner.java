package bleuauction.bleuauction_be.server.util;

import lombok.Data;

@Data
public class JwtReturner {

  private String accessToken;
  private String RefToken;

  public JwtReturner(String accessToken, String refToken) {
    this.accessToken = accessToken;
    RefToken = refToken;
  }
}