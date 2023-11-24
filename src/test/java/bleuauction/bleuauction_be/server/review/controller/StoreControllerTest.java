package bleuauction.bleuauction_be.server.review.controller;

import bleuauction.bleuauction_be.server.store.controller.StoreController;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import bleuauction.bleuauction_be.server.store.service.StoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class StoreControllerTest {

  @Mock
  private StoreService storeService;

  @InjectMocks
  private StoreController storeController;

  private MockMvc mockMvc;

  @Test
  @DisplayName("/api/store/list로 Get 요청을 보낼 경우 OK 상태코드가 반환된다.")
  public void testStoreList() throws Exception {
    int startPage = 0;
    int pageLowCount = 3;
    String authorizationHeader = "UA";

    List<Store> mockStoreList = new ArrayList<>();
    when(storeService.selectStoreList(StoreStatus.Y, startPage, pageLowCount)).thenReturn(mockStoreList);

    mockMvc = MockMvcBuilders.standaloneSetup(storeController).build();
    mockMvc.perform(MockMvcRequestBuilders.get("/api/store/list")
                    .header("Authorization", authorizationHeader)
                    .param("startPage", String.valueOf(startPage))
                    .param("pageLowCount", String.valueOf(pageLowCount))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());

    verify(storeService, times(1)).selectStoreList(StoreStatus.Y, startPage, pageLowCount);
  }
}