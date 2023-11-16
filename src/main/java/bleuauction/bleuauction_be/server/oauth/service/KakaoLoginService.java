package bleuauction.bleuauction_be.server.oauth.service;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.service.MemberService;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.Random;

/*
 * TODO : 문자열 전부 Properties에서 주입하도록 변경 및 통신 객체 RestTemplate 또는 Spring Cloud OpenFeign으로 변경 필요하며, RestTemplate로만 Mock객체 생성가능하기 RestTemplate를 채택해야 한다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    private final MemberService memberService;

    public String getKaKaoAccessToken(String code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=8f86e26b25885745851af320068795ef"); // TODO REST_API_KEY 입력
            sb.append(
                    "&redirect_uri=http://localhost:8080/oauth/kakao"); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("responseCode : " + responseCode);
            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            log.info("access_token : " + access_Token);
            log.info("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return access_Token;
    }

    public void createKakaoUser(String token) throws Exception {

        String reqURL = "https://kapi.kakao.com/v2/user/me";

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization",
                    "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();

            log.info("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            Long id = element.getAsJsonObject().get("id").getAsLong();
            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject()
                    .get("has_email").getAsBoolean();
            String email = "";
            if (hasEmail) {
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject()
                        .get("email").getAsString();
            }
            Optional<Member> member = memberService.findByMemberEmail(email);

            if (member.isEmpty()) {
                Member newMember = new Member();
                newMember.setMemberEmail(email);
                newMember.setMemberPwd(createRandomPassword(12));
                // 다른 속성 설정

                try {
                    memberService.signUp(newMember);
                } catch (Exception e) {
                    log.error("Failed to sign up new member: " + e.getMessage());
                }
            }
            log.info("User email: " + email);
            br.close();
        } catch (IOException e) {
            log.error("Failed to create Kakao user: " + e.getMessage());
        }
    }

    public static String createRandomPassword(int length) {
        String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        // 랜덤 객체 생성
        Random random = new Random();
        // 문자열 길이만큼 반복하여 랜덤 문자열 생성
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(charset.length());
            char randomChar = charset.charAt(randomIndex);
            randomString.append(randomChar);
        }
        return randomString.toString();
    }
}