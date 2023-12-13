package bleuauction.bleuauction_be.server.common.jwt;

import bleuauction.bleuauction_be.server.common.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.util.Map;


@Slf4j

public class APILoginFilter extends AbstractAuthenticationProcessingFilter {
  public APILoginFilter(String defaultFilterProcessesUrl) {
    super(defaultFilterProcessesUrl);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    log.info("APILoginFilter 실행 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

    Map<String, String> jsonData = JsonUtils.parseRequestJSON(request);
    log.info("jsonData >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> {}", jsonData);

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            jsonData.get("memberEmail"),
            jsonData.get("memberPwd"));

    return getAuthenticationManager().authenticate(authenticationToken);
  }

}
