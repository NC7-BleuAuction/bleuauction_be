package bleuauction.bleuauction_be.server.menu.service;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.util.MemberEntityFactory;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuSize;
import bleuauction.bleuauction_be.server.menu.entity.MenuStatus;
import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import bleuauction.bleuauction_be.server.store.entity.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;

    @InjectMocks
    MenuService menuService;

    @Test
    @DisplayName("메뉴 등록")
    void insertMenu() {
        //given
        Member mockMember = MemberEntityFactory.of("test@test", "testPassword123!@#", "테스트입니다", MemberCategory.S);
        Store store = new Store();
        Menu menu = new Menu();
        List<MultipartFile> multipartFiles = new ArrayList<>();

        menu.setStoreNo(store);
        menu.setMenuSize(MenuSize.L);
        menu.setMenuContent("내용");
        menu.setMenuName("매운탕");
        menu.setMenuPrice(10000);

        //when
        Long testmenu = menuService.enroll(menu, store, multipartFiles);

        //then
        assertEquals(menu.getMenuNo(), testmenu);

    }

    @Test
    @DisplayName("가게 번호로 메뉴 찾기")
    void findMenusByStoreNo() {
        //given
        Store store = new Store();
        Menu menu = new Menu();

        menu.setStoreNo(store);
        menu.setMenuSize(MenuSize.L);
        menu.setMenuContent("내용");
        menu.setMenuName("매운탕");
        menu.setMenuPrice(10000);
        menu.setMenuStatus(MenuStatus.Y);
        menuRepository.save(menu);


        Menu menu1 = new Menu();
        menu1.setStoreNo(store);
        menu1.setMenuSize(MenuSize.L);
        menu1.setMenuContent("내용1");
        menu1.setMenuName("매운탕1");
        menu1.setMenuPrice(100000);
        menu1.setMenuStatus(MenuStatus.Y);
        menuRepository.save(menu1);

        Menu menu2 = new Menu();
        menu2.setStoreNo(store);
        menu2.setMenuSize(MenuSize.S);
        menu2.setMenuContent("내용2");
        menu2.setMenuName("매운탕2");
        menu2.setMenuPrice(100000);
        menu2.setMenuStatus(MenuStatus.Y);
        menuRepository.save(menu2);


        //when
        when(menuRepository.findMenusByStoreNoAndMenuStatus(store.getStoreNo(), MenuStatus.Y)).thenReturn(Arrays.asList(menu, menu1, menu2));

        List<Menu> lists = menuService.findMenusByStoreNo(store.getStoreNo());

        // then
        assertEquals(menu2.getMenuContent(), "내용2");
        assertEquals(3, lists.size());

    }

    @Test
    @DisplayName("가게 번호로 메뉴 찾기2")
    void findMenusByStoreNoAndStatus() {
        //given
        Store store = new Store();
        Menu menu = new Menu();

        menu.setStoreNo(store);
        menu.setMenuSize(MenuSize.L);
        menu.setMenuContent("내용");
        menu.setMenuName("매운탕");
        menu.setMenuPrice(10000);
        menu.setMenuStatus(MenuStatus.Y);
        menuRepository.save(menu);


        Menu menu1 = new Menu();
        menu1.setStoreNo(store);
        menu1.setMenuSize(MenuSize.L);
        menu1.setMenuContent("내용1");
        menu1.setMenuName("매운탕1");
        menu1.setMenuPrice(100000);
        menu1.setMenuStatus(MenuStatus.Y);
        menuRepository.save(menu1);

        Menu menu2 = new Menu();
        menu2.setStoreNo(store);
        menu2.setMenuSize(MenuSize.S);
        menu2.setMenuContent("내용2");
        menu2.setMenuName("매운탕2");
        menu2.setMenuPrice(100000);
        menu2.setMenuStatus(MenuStatus.Y);
        menuRepository.save(menu2);


        //when
        when(menuRepository.findMenusByStoreNoAndMenuStatus(store.getStoreNo(), MenuStatus.Y)).thenReturn(Arrays.asList(menu, menu1, menu2));

        List<Menu> lists = menuService.findMenusByStoreNoAndStatus(store.getStoreNo(), MenuStatus.Y);

        // then
        assertEquals(menu2.getMenuContent(), "내용2");
        assertEquals(3, lists.size());


    }

    @Test
    void findOne() {
        //given
        Store store = new Store();
        Menu menu = new Menu();

        menu.setStoreNo(store);
        menu.setMenuSize(MenuSize.L);
        menu.setMenuContent("내용");
        menu.setMenuName("매운탕");
        menu.setMenuPrice(10000);
        menu.setMenuStatus(MenuStatus.Y);
        menuRepository.save(menu);

        //when
        when(menuRepository.findMenusByMenuNo(menu.getMenuNo())).thenReturn(menu);
        Menu findMenu = menuService.findOne(menu.getMenuNo());

        //then
        assertEquals(findMenu, menu);

    }



}