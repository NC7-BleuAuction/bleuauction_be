package bleuauction.bleuauction_be;

import bleuauction.bleuauction_be.notice.service.NoticeService;
import bleuauction.bleuauction_be.notice.web.NoticeForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import bleuauction.bleuauction_be.notice.service.NoticeService;


@Slf4j
@RestController
public class TestController {

    @GetMapping("/health")
    public ResponseEntity healthCheck() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/hello")
    public String hello() {
        log.info("helllllo");
        return "Hello1";
    }

}
