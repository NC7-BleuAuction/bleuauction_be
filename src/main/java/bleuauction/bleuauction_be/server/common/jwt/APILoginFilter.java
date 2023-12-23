package bleuauction.bleuauction_be.server.common.jwt;


import bleuauction.bleuauction_be.server.common.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Slf4j
public class APILoginFilter extends AbstractAuthenticationProcessingFilter {

    public APILoginFilter(
            String defaultFilterProcessesUrl,
            AuthenticationSuccessHandler authenticationSuccessHandler) {
        super(defaultFilterProcessesUrl);
        super.setAuthenticationSuccessHandler(authenticationSuccessHandler);
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        log.info("APILoginFilter 실행 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Map<String, String> jsonData = JsonUtils.parseRequestJSON(request);
        log.info("jsonData >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> {}", jsonData);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        jsonData.get("memberEmail"), jsonData.get("memberPwd"));

        return getAuthenticationManager().authenticate(authenticationToken);
    }
}
