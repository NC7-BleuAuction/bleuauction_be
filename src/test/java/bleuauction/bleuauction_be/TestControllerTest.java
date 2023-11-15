package bleuauction.bleuauction_be;

import bleuauction.bleuauction_be.server.store.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(
        controllers = TestController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfiguration.class)
        }
)
class TestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StoreService storeService;

    @Test
    @DisplayName("응애")
    void 응애응애() {
        log.info("오하요 고자이마스");
    }
    @Test
    @WithMockUser
    @DisplayName("/health로 Get요청을 보낼 경우 OK 상태코드가 반환된다.")
    void healthCheckTest() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.get("/health")
                        .with(csrf())
        ).andExpect(status().isOk());
    }
    @Test
    @WithMockUser
    @DisplayName("/api/test 로 Get요청을 보낼 경우 자동 CI/CD Test문자열로 반환이 되어야 한다.")
    void helloMethodTest() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.get("/api/test")
                        .with(csrf())
        ).andExpect(status().isOk())
        .andExpect(content().string("자동 CI/CD Test"));
    }
}