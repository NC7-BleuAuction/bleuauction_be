package bleuauction.bleuauction_be.server.oauth.controller;

import bleuauction.bleuauction_be.server.oauth.service.KakaoLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
