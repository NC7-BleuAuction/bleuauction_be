package bleuauction.bleuauction_be.server.oauth.controller;

import bleuauction.bleuauction_be.server.member.service.MemberService;
import bleuauction.bleuauction_be.server.oauth.service.KakaoLoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class KakaoLoginController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/oauth/kakao")
    public ResponseEntity<String> kakaoCallback(@RequestParam String code, HttpServletRequest request) {
        log.info(code);
        try {
            String access_Token = KakaoLoginService.getKaKaoAccessToken(code);
            KakaoLoginService.createKakaoUser(access_Token, request, memberService);
            log.info("Kakao callback code: {}", code);
            return ResponseEntity.ok("{'message': 'Redirecting to Main'}");
        } catch (Exception e) {
            log.error("Exception occurred: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("{'error': 'An error occurred'}");
        }
    }
}
