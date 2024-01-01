package bleuauction.bleuauction_be.server.menu.service;//package bleuauction.bleuauction_be.server.menu.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.util.MemberEntityFactory;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuSize;
import bleuauction.bleuauction_be.server.menu.entity.MenuStatus;
import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.util.StoreUtilFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuModuleServiceTest {

    @Mock MenuRepository menuRepository;

    @InjectMocks MenuModuleService menuModuleService;



    @Test
    @DisplayName("가게 번호로 메뉴 찾기")
    void findMenusByStoreNo() {
        // given
        Member mockSeller =
                MemberEntityFactory.of("test@test.com", "1111111", "테스트아무개", MemberCategory.S);
        Store store = StoreUtilFactory.of(mockSeller, "노량진수산시장", "가게이름1", "111-11-11111");
        store.setId(1L);

        Menu menu =
                Menu.builder()
                        .store(store)
                        .size(MenuSize.L)
                        .content("내용")
                        .name("매운탕")
                        .price(10000)
                        .status(MenuStatus.Y)
                        .build();
        menuRepository.save(menu);

        Menu menu1 =
                Menu.builder()
                        .store(store)
                        .size(MenuSize.M)
                        .content("내용1")
                        .name("매운탕1")
                        .price(100000)
                        .status(MenuStatus.Y)
                        .build();
        menuRepository.save(menu1);

        Menu menu2 =
                Menu.builder()
                        .store(store)
                        .size(MenuSize.S)
                        .content("내용2")
                        .name("매운탕2")
                        .price(1000000)
                        .status(MenuStatus.Y)
                        .build();
        menuRepository.save(menu2);

        // when
        when(menuRepository.findAllByStoreAndStatus(store, MenuStatus.Y))
                .thenReturn(Arrays.asList(menu, menu1, menu2));

        List<Menu> lists = menuModuleService.findAllByStore(store);

        // then
        assertEquals(menu2.getContent(), "내용2");
        assertEquals(3, lists.size());
    }

    @Test
    @DisplayName("가게 번호로 메뉴 찾기2")
    void findMenusByStoreNoAndStatus() {
        // given
        Member mockSeller =
                MemberEntityFactory.of("test@test.com", "1111111", "테스트아무개", MemberCategory.S);
        Store mockStore = StoreUtilFactory.of(mockSeller, "노량진수산시장", "가게이름1", "111-11-11111");
        mockStore.setId(1L);

        Menu menu =
                Menu.builder()
                        .store(mockStore)
                        .size(MenuSize.L)
                        .content("내용")
                        .name("매운탕")
                        .price(10000)
                        .status(MenuStatus.Y)
                        .build();
        menuRepository.save(menu);

        Menu menu1 =
                Menu.builder()
                        .store(mockStore)
                        .size(MenuSize.L)
                        .content("내용1")
                        .name("매운탕1")
                        .price(100000)
                        .status(MenuStatus.Y)
                        .build();
        menuRepository.save(menu1);

        Menu menu2 =
                Menu.builder()
                        .store(mockStore)
                        .size(MenuSize.S)
                        .content("내용2")
                        .name("매운탕2")
                        .price(1000000)
                        .status(MenuStatus.Y)
                        .build();
        menuRepository.save(menu2);

        // when
        when(menuRepository.findAllByStoreAndStatus(mockStore, MenuStatus.Y))
                .thenReturn(Arrays.asList(menu, menu1, menu2));

        List<Menu> lists = menuModuleService.findAllByStore(mockStore);

        // then
        assertEquals(menu2.getContent(), "내용2");
        assertEquals(3, lists.size());
    }

    @Test
    void findOne() {
        // given
        Member mockSeller =
                MemberEntityFactory.of("test@test.com", "1111111", "테스트아무개", MemberCategory.S);
        Store mockStore = StoreUtilFactory.of(mockSeller, "노량진수산시장", "가게이름1", "111-11-11111");
        mockStore.setId(1L);

        Menu menu =
                Menu.builder()
                        .store(mockStore)
                        .size(MenuSize.L)
                        .content("내용")
                        .name("매운탕")
                        .price(10000)
                        .status(MenuStatus.Y)
                        .build();
        menu.setId(1L);
        menuRepository.save(menu);

        // when
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        Menu findMenu = menuModuleService.findOne(menu.getId());

        // then
        assertEquals(findMenu, menu);
    }


}
