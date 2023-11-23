package bleuauction.bleuauction_be.server.review.controller;

import bleuauction.bleuauction_be.server.review.service.ReviewService;
import bleuauction.bleuauction_be.server.common.jwt.CreateJwt;
import bleuauction.bleuauction_be.server.common.jwt.TokenMember;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@ActiveProfiles({"auth", "pay", "jwt", "test"})
@WebMvcTest(
        controllers = ReviewController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfiguration.class)
        }
)
public class ReviewControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ReviewService reviewService;
  @Autowired
  private CreateJwt createJwt;

  @Autowired
  private ReviewController reviewController;

  private String accessToken;


  @BeforeEach
  public void setup() {
    // 토큰 미리 발급
    TokenMember tokenMember = new TokenMember(1L, "itkw87@naver.com", "최기현", "M");
    log.info("createJwt ===========================> {}", createJwt);
    accessToken = createJwt.createAccessToken(tokenMember);
    log.info("accessToken ===========================> {}", accessToken);
  }

  @Test
  @WithMockUser
  @DisplayName("/api/reviews로 Get 요청을 보낼 경우 모든 리뷰 리스트를 조회하고 성공시 OK 상태코드가 반환된다.")
  public void testReview() throws Exception {
    String authorizationHeader = "Bearer " + accessToken;
    Long storeNo = 1L;
    int startPage = 0;

    mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    mockMvc.perform(MockMvcRequestBuilders.get("/api/reviews")
                    .header("Authorization", authorizationHeader)
                    .param("storeNo", String.valueOf(storeNo))
                    .param("startPage", String.valueOf(startPage))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());

    // storeService.selectStoreList 메서드가 예상된 매개변수로 호출되었는지 확인합니다.
    verify(reviewService, times(1)).selectReviewList(storeNo, startPage);
  }
}

