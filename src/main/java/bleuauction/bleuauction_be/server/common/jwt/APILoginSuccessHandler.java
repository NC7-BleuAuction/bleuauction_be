package bleuauction.bleuauction_be.server.common.jwt;


import bleuauction.bleuauction_be.server.common.utils.JwtUtils;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Slf4j
@RequiredArgsConstructor
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        log.info("APILoginSuccessHandler 실행 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        log.info(
                "onAuthenticationSuccess() - authentication  >>>>>>>>>>>>>>>>>>>>>>>>> {}",
                authentication);

        APIUserDTO apiUserDTO = (APIUserDTO) authentication.getPrincipal();

        log.info(
                "onAuthenticationSuccess() - APIUserDTO  >>>>>>>>>>>>>>>>>>>>>>>>> {}",
                apiUserDTO.toString());

        String accessToken =
                jwtUtils.createAccessToken(
                        new TokenMember(
                                apiUserDTO.getMemberNo(),
                                apiUserDTO.getMemberEmail(),
                                apiUserDTO.getMemberName(),
                                apiUserDTO.getMemberCategory()));
        String refreshToken = jwtUtils.createRefreshToken(TokenMember.of(accessToken), accessToken);

        Gson gson = new Gson();

        Map<String, String> keyMap =
                Map.of(
                        "accessToken", accessToken,
                        "refreshToken", refreshToken);

        String jsonStr = gson.toJson(keyMap);
        response.getWriter().println(jsonStr);
    }
}
