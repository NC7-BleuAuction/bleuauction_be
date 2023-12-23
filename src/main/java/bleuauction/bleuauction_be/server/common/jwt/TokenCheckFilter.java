package bleuauction.bleuauction_be.server.common.jwt;


import bleuauction.bleuauction_be.server.common.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {

    private final APIUserDetailsService apiUserDetailsService;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        log.info("authorizationHeader >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> {}", authorizationHeader);

        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("TokenCheckFilter 실행 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        jwtUtils.verifyToken(authorizationHeader);
        String memberEmail = jwtUtils.getTokenMember(authorizationHeader).getMemberEmail();

        APIUserDTO apiUserDTO = (APIUserDTO) apiUserDetailsService.loadUserByUsername(memberEmail);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        apiUserDTO, null, apiUserDTO.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
