package bleuauction.bleuauction_be;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/health")
    public ResponseEntity healthCheck() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/test")
    public String hello() {
        log.info("hello");
        return "자동 CI/CD Test";
    }
}
