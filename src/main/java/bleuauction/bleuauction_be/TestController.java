package bleuauction.bleuauction_be;

import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.service.StoreService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
//@RestController
public class TestController {

    @Autowired
    StoreService storeService;

    @GetMapping("/health")
    public ResponseEntity healthCheck() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/hello")
    public String hello() {
        log.info("hello");
        return "자동 CI/CD Test";
    }


}
