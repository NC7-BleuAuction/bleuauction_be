package bleuauction.bleuauction_be.server.common.jwt;

import lombok.Getter;

@Getter
public class RefreshTokenRequest {
  private String refreshToken;
}