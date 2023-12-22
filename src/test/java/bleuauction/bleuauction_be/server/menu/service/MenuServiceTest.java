package bleuauction.bleuauction_be.server.menu.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.AttachVO;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.entity.MenuAttach;
import bleuauction.bleuauction_be.server.attach.service.AttachComponentService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock MenuRepository menuRepository;

    @Mock MenuModuleService menuModuleServiceM;

    @Mock AttachComponentService attachComponentService;

    @InjectMocks MenuModuleService menuModuleService;

    @InjectMocks MenuComponentService menuComponentService;

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
        when(menuModuleServiceM.save(any(Menu.class)))
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

    @Test
    @DisplayName("삭제하기")
    void deleteMenuByMenuNoAndStore() {
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
        menu.setId(100L);

        Attach attach1 =
                new MenuAttach(
                        AttachVO.builder()
                                .filePath("FilePath")
                                .originFilename("originFilename")
                                .saveFileName("saveFilename")
                                .fileStatus(FileStatus.Y)
                                .build(),
                        menu);
        menuRepository.save(menu);

        when(menuRepository.findById(menu.getId())).thenReturn(Optional.of(menu));

        // when
        menuComponentService.deleteMenuByMenuNoAndStore(menu.getId(), mockStore);

        // then
        Assertions.assertEquals(menu.getStatus(), MenuStatus.N);
        Assertions.assertEquals(attach1.getFileStatus(), FileStatus.N);
    }

    @Test
    @DisplayName("수정하기")
    void update() {
        // given
        Member mockSeller =
                MemberEntityFactory.of("test@test.com", "1111111", "테스트아무개", MemberCategory.S);
        Store mockStore = StoreUtilFactory.of(mockSeller, "노량진수산시장", "가게이름1", "111-11-11111");
        mockStore.setId(1L);

        long menuNo = 1L;

        List<MultipartFile> multipartFiles = new ArrayList<>();
        Menu existedmenu =
                Menu.builder()
                        .store(mockStore)
                        .size(MenuSize.L)
                        .content("기존 내용")
                        .name("기존 이름")
                        .price(10000)
                        .status(MenuStatus.Y)
                        .build();
        existedmenu.setId(menuNo);

        Attach attach1 =
                new MenuAttach(
                        AttachVO.builder()
                                .filePath("FilePath")
                                .originFilename("originFilename")
                                .saveFileName("saveFilename")
                                .fileStatus(FileStatus.Y)
                                .build(),
                        existedmenu);

        menuRepository.save(existedmenu);

        // 업데이트할 내용을 담은 새로운 Menu 객체 생성
        Menu updatemenu =
                Menu.builder()
                        .store(mockStore)
                        .size(MenuSize.L)
                        .content("수정 내용")
                        .name("수정 이름")
                        .price(20000)
                        .status(MenuStatus.Y)
                        .build();

        // findOne 호출될 때 existedmenu를 리턴하도록 설정
        when(menuModuleServiceM.findOne(menuNo)).thenReturn(updatemenu);
        when(menuModuleServiceM.findOne(updatemenu.getId())).thenReturn(existedmenu);

        // when
        menuComponentService.update(1L, multipartFiles, mockStore);

        // then
        assertEquals(existedmenu.getId(), updatemenu.getId());
        assertEquals(20000, updatemenu.getPrice());
    }
}
