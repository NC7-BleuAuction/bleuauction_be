package bleuauction.bleuauction_be.server.common.jwt;

import bleuauction.bleuauction_be.server.exception.BaseException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Setter
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
  private final CreateJwt createJwt;
  private static final List<String> URI_WHITE_LIST = List.of("/api/member/login", "/api/member/signup");

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    log.info("JwtTokenFilter 시작 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

    String uriPath = request.getRequestURI();
    if (URI_WHITE_LIST.contains(uriPath)) {
      filterChain.doFilter(request, response);
      log.info("해당 요청은 인가 과정 불필요 URI: ===========> {}", uriPath);
      return;
    }

    String authorizationHeader = request.getHeader("Authorization");

    try {
      createJwt.verifyAccessToken(authorizationHeader);
      TokenMember tokenMember = createJwt.getTokenMember(authorizationHeader);

      UserDetails userDetail = new CustomUserDetails(
              String.valueOf(tokenMember.getMemberNo()),
              tokenMember.getMemberEmail(),
              tokenMember.getMemberName(),
              Collections.singleton(new SimpleGrantedAuthority(tokenMember.getMemberCategory().name()))
      );

      Authentication authentication = new UsernamePasswordAuthenticationToken(
              userDetail,
              null,
              userDetail.getAuthorities()
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);
      log.info("JwtTokenFilter 끝 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

      filterChain.doFilter(request, response);
    } catch (BaseException e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("올바르지 않은 권한입니다.");
    }
  }
}
