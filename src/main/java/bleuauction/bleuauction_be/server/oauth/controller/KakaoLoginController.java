package bleuauction.bleuauction_be.server.oauth.controller;

import bleuauction.bleuauction_be.server.oauth.service.KakaoLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * TODO : 문자열 전부 Properties에서 주입하도록 변경 및 통신 객체 RestTemplate 또는 Spring Cloud OpenFeign으로 변경 필요하며, RestTemplate로만 Mock객체 생성가능하기 RestTemplate를 채택해야 한다.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/oauth/kakao")
    public ResponseEntity<String> kakaoCallback(@RequestParam String code) {
        log.info(code);
        try {
            String access_Token = kakaoLoginService.getKaKaoAccessToken(code);
            kakaoLoginService.createKakaoUser(access_Token);
            log.info("Kakao callback code: {}", code);
            return ResponseEntity.ok("{'message': 'Redirecting to Main'}");
        } catch (Exception e) {
            log.error("Exception occurred: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("{'error': 'An error occurred'}");
        }
    }
}
