package bleuauction.bleuauction_be.server.menu.service;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.util.MemberEntityFactory;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuSize;
import bleuauction.bleuauction_be.server.menu.entity.MenuStatus;
import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.util.StoreUtilFactory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuComponentServiceTest {


    @Mock
    private MenuModuleService menuModuleService;

    @InjectMocks
    private MenuComponentService menuComponentService;


    @Test
    @DisplayName("메뉴 등록")
    @Transactional
    void insertMenu() {
        // given
        Member mockMember =
                MemberEntityFactory.of(
                        "test@test", "testPassword123!@#", "테스트입니다", MemberCategory.S);
        Store store = StoreUtilFactory.of(mockMember, "노량진수산시장", "가게이름1", "111-11-11111");
        store.setId(1L);

        List<MultipartFile> multipartFiles =
                Collections.singletonList(
                        new MockMultipartFile(
                                "file", "filename.txt", "text/plain", "content".getBytes()));

        Menu menu =
                Menu.builder()
                        .store(store)
                        .size(MenuSize.L)
                        .content("내용")
                        .name("매운탕")
                        .price(10000)
                        .status(MenuStatus.Y)
                        .build();

        // when
        when(menuModuleService.save(any(Menu.class)))
                .then(
                        invocation -> {
                            Menu menu1 = invocation.getArgument(0);
                            menu1.setId(1L);
                            return menu1;
                        });
        Long testmenu = menuComponentService.enroll(menu, store, multipartFiles);

        // then
        assertEquals(menu.getId(), testmenu);
    }

}