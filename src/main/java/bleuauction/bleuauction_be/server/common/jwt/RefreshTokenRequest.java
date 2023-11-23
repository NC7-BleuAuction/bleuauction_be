package bleuauction.bleuauction_be.server.common.jwt;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RefreshTokenRequest {
  @NotNull(message = "Refresh Token이 Null이면 요청이 불가합니다.₩")
  private String refreshToken;
}