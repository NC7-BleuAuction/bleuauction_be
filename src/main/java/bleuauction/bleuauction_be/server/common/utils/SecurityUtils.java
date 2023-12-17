package bleuauction.bleuauction_be.server.common.utils;

import bleuauction.bleuauction_be.server.common.jwt.APIUserDTO;
import bleuauction.bleuauction_be.server.member.entity.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

  public static Member getAuthenticatedUserToMember() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    APIUserDTO apiUserDTO = (APIUserDTO)authentication.getPrincipal();
    return apiUserDTO.toMemberEntity();
  }
}
