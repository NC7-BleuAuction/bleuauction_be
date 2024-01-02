package bleuauction.bleuauction_be.server.menu.service;//package bleuauction.bleuauction_be.server.menu.service;

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
import bleuauction.bleuauction_be.server.menu.dto.MenuDTO;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.menu.entity.MenuSize;
import bleuauction.bleuauction_be.server.menu.entity.MenuStatus;
import bleuauction.bleuauction_be.server.menu.repository.MenuRepository;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.util.StoreUtilFactory;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
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
class MenuComponentServiceTest {

    @Mock MenuRepository menuRepository;

    @Mock MenuModuleService menuModuleServiceM;

    @Mock
    private AttachComponentService attachComponentService;

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

        Menu mockMenu =
                Menu.builder()
                        .store(store)
                        .size(MenuSize.L)
                        .content("내용")
                        .name("매운탕")
                        .price(10000)
                        .status(MenuStatus.Y)
                        .build();

        // when
        when(menuModuleServiceM.save(mockMenu)).thenReturn(mockMenu);
        Long result = menuComponentService.enroll(mockMenu, store, multipartFiles);

        // then
        assertEquals(mockMenu.getId(), result);
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
        mockSeller.setId(1L);

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
        MenuDTO request =
                MenuDTO.builder()
                        .size(MenuSize.L)
                        .content("수정 내용")
                        .name("수정 이름")
                        .price(20000)
                        .build();

        // findOne 호출될 때 existedmenu를 리턴하도록 설정
        when(menuModuleServiceM.findOne(menuNo)).thenReturn(existedmenu);

        // when
        menuComponentService.update(1L, multipartFiles, mockStore, request);

        // then
        assertEquals(existedmenu.getName(), request.getName());
        assertEquals(20000, existedmenu.getPrice());
    }
}
