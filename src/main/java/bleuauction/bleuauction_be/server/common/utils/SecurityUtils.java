package bleuauction.bleuauction_be.server.common.utils;


import bleuauction.bleuauction_be.server.common.exception.ForbiddenAccessException;
import bleuauction.bleuauction_be.server.common.jwt.APIUserDTO;
import bleuauction.bleuauction_be.server.member.entity.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    /**
     * Authentication 객체 -> Member 객체 변환 로직
     *
     * @return
     */
    public static Member getAuthenticatedUserToMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        APIUserDTO apiUserDTO = (APIUserDTO) authentication.getPrincipal();
        return apiUserDTO.toMemberEntity();
    }

    /**
     * SecurityContextHolder에 보관된 Member의 No와 파라미터로 받은 MemberNo를 비교한다. 일치하지 않는 경우
     * ForbiddenAccessException이 발생한다.
     *
     * @param memberNo
     * @throws ForbiddenAccessException
     */
    public static void checkOwnsByMemberNo(Long memberNo) {
        if (getAuthenticatedUserToMember().getId() != memberNo) {
            throw ForbiddenAccessException.EXCEPTION;
        }
    }
}
