package bleuauction.bleuauction_be.server.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TokenMember {
    private Long memberNo;

    private String memberEmail;

    private String memberName;

    private String memberCategory;
}

