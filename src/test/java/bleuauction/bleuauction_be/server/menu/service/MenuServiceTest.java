package bleuauction.bleuauction_be.server.menu.service;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.repository.AttachRepository;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.member.util.MemberEntityFactory;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuSize;
import bleuauction.bleuauction_be.server.menu.entity.MenuStatus;
import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import bleuauction.bleuauction_be.server.store.entity.Store;
import org.junit.jupiter.api.Assertions;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;

    @Mock
    AttachRepository attachRepository;

    @InjectMocks
    MenuService menuService;

    @Mock
    AttachService attachService;

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

    @Test
    @DisplayName("삭제하기")
    void deleteMenuByMenuNoAndStore() {
        //given
        Store store = new Store();
        Menu menu = new Menu();
        menu.setMenuNo(100L);

        Attach attach1 = new Attach();
        attach1.setMenuNo(menu);
        attach1.setFilePath("FilePath");
        attach1.setOriginFilename("originFilename");
        attach1.setSaveFilename("saveFilename");
        attach1.setFileStatus(FileStatus.Y);
        attachRepository.save(attach1);



        menu.setStoreNo(store);
        menu.setMenuSize(MenuSize.L);
        menu.setMenuContent("내용");
        menu.setMenuName("매운탕");
        menu.setMenuPrice(10000);
        menu.addAttach(attach1);
        menuRepository.save(menu);

        when(menuRepository.findMenusByMenuNo(menu.getMenuNo())).thenReturn(menu);
        when(attachService.changeFileStatusToDeleteByFileNo(attach1.getFileNo())).thenAnswer(invocation -> {
            attach1.changeFileStatusToDelete();
            return null;
        });

        //when
        menuService.deleteMenuByMenuNoAndStore(menu.getMenuNo(), store);

        //then
        Assertions.assertEquals(menu.getMenuStatus(), MenuStatus.N);
        Assertions.assertEquals(attach1.getFileStatus(), FileStatus.N);

    }

    @Test
    @DisplayName("수정하기")
    void update() {
        //given
        Store store = new Store();
        Menu existedmenu = new Menu();
        List<MultipartFile> multipartFiles = new ArrayList<>();

        Attach attach1 = new Attach();
        attach1.setMenuNo(existedmenu);
        attach1.setFilePath("FilePath");
        attach1.setOriginFilename("originFilename");
        attach1.setSaveFilename("saveFilename");
        attach1.setFileStatus(FileStatus.Y);
        attachRepository.save(attach1);

        existedmenu.setStoreNo(store);
        existedmenu.setMenuSize(MenuSize.L);
        existedmenu.setMenuContent("기존 내용");
        existedmenu.setMenuName("기존 이름");
        existedmenu.setMenuPrice(10000);
        menuRepository.save(existedmenu);

        // 업데이트할 내용을 담은 새로운 Menu 객체 생성
        Menu updatemenu = new Menu();
        updatemenu.setMenuNo(existedmenu.getMenuNo());
        updatemenu.setStoreNo(store);
        updatemenu.setMenuSize(MenuSize.L);
        updatemenu.setMenuContent("수정 내용");
        updatemenu.setMenuName("수정 이름");
        updatemenu.setMenuPrice(20000);

        // findOne 호출될 때 existedmenu를 리턴하도록 설정
        when(menuRepository.findMenusByMenuNo(existedmenu.getMenuNo())).thenReturn(existedmenu);


        //when
        menuService.update(updatemenu,multipartFiles, store);

        //then
        verify(menuRepository, times(1)).findMenusByMenuNo(updatemenu.getMenuNo());

        assertEquals(existedmenu.getMenuNo(), updatemenu.getMenuNo());
        assertEquals(existedmenu.getMenuPrice(), updatemenu.getMenuPrice());

    }


}