package bleuauction.bleuauction_be.server.common.utils;


import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JsonUtils {

    public static Map<String, String> parseRequestJSON(HttpServletRequest request) {
        try (Reader reader = new InputStreamReader(request.getInputStream())) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Map.class);

        } catch (Exception e) {
            log.error("parseRequestJSON() 호출 중 에러 발생! >>>>>>>>>>>>>>>>>> {}", e.getMessage());
        }
        return null;
    }
}
