package bleuauction.bleuauction_be.server.util;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenMember {

  @NotNull
  private String memberEmail;

  @NotNull
  private String memberName;

  public TokenMember(String memberEmail, String memberName) {
    this.memberEmail = memberEmail;
    this.memberName = memberName;
  }
}
