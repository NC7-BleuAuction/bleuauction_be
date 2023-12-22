package bleuauction.bleuauction_be.server.common.jwt;


import bleuauction.bleuauction_be.server.common.utils.JsonUtils;
import bleuauction.bleuauction_be.server.common.utils.JwtUtils;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {

    private final String refreshPath;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (!path.equals(refreshPath)) { // 리프레쉬 토큰 발급 요청 아니면 패스
            log.info("RefreshTokenFilter 스킵 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("RefreshTokenFilter 실행 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        Map<String, String> tokens = JsonUtils.parseRequestJSON(request);
        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        log.info("accessToken >>>>>>>>>>>>>>>>>>>>>>>>> {}", accessToken);
        log.info("refreshToken >>>>>>>>>>>>>>>>>>>>>>>>> {}", refreshToken);

        String renewAccessToken = jwtUtils.getRenewAccessToken(refreshToken);

        String renewRefreshToken =
                jwtUtils.isReissuanceRefreshToken(refreshToken) // 리프레쉬 토큰의 남은 유효기간이 재발급 기준에 해당할 경우
                        ? jwtUtils.createRefreshToken(
                                TokenMember.of(renewAccessToken), renewAccessToken)
                        : refreshToken;

        sendToken(renewAccessToken, renewRefreshToken, response);
    }

    private void sendToken(
            String accessToeknValue, String refreshTokenValue, HttpServletResponse response)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String jsonStr =
                gson.toJson(
                        Map.of(
                                "accessToken", accessToeknValue,
                                "refreshToken", refreshTokenValue));
        response.getWriter().println(jsonStr);
    }
}
