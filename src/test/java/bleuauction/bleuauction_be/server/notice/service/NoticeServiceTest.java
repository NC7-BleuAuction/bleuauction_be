package bleuauction.bleuauction_be.server.notice.service;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.repository.AttachRepository;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.notice.entity.NoticeStatus;
import bleuauction.bleuauction_be.server.notice.repository.NoticeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Mockito의 기능을 JUnit 5 테스트에 활성화시켜 Mockito 관련 애노테이션을 사용가능
class NoticeServiceTest {

    @Mock
    AttachRepository attachRepository;
    @Mock // 가짜 객체
    private NoticeRepository noticeRepository;

    @InjectMocks // 가짜(Mock) 객체(@Mock로 표시된 객체)를 테스트 대상 객체에 자동으로 주입할 수 있고 이를 통해 주입된 가짜 객체를 사용하여 테스트 대상 객체의 메소드를 호출하고 동작을 검증할 수 있습니다.
    private NoticeComponentService noticeComponentService;

    @InjectMocks
    private NoticeModuleService noticeModuleService;


    @Test
    void testEnroll() {
        // Given
        Member mockMember = new Member();
        mockMember.setMemberCategory(MemberCategory.A);

        Notice mockNotice = new Notice();
        mockNotice.setNoticeNo(1L);
        mockNotice.setMemberNo(mockMember);

        List<MultipartFile> multipartFiles = new ArrayList<>();

        // When
        when(noticeRepository.save(any(Notice.class))).thenReturn(mockNotice);
        Long result = noticeComponentService.enroll(mockNotice, multipartFiles, mockMember);

        // Then
        assertEquals(mockNotice.getNoticeNo(), result);
    }

    @Test
    @DisplayName("노티스 수정")
    void testUpdateNotice() throws Exception {

        //given
        Member mockMember = new Member();
        mockMember.setMemberCategory(MemberCategory.A);
        Notice existingNotice = new Notice();
        List<MultipartFile> multipartFiles = new ArrayList<>();

        Attach attach1 = new Attach();
        attach1.setNoticeNo(existingNotice);
        attach1.setFilePath("FilePath");
        attach1.setOriginFilename("originFilename");
        attach1.setSaveFilename("saveFilename");
        attach1.setFileStatus(FileStatus.Y);
        attachRepository.save(attach1);

        existingNotice.setNoticeTitle("기존 제목");
        existingNotice.setNoticeContent("기존 내용");
        noticeRepository.save(existingNotice);

        // 업데이트할 내용을 담은 새로운 Notice 객체 생성
        Notice updatedNotice = new Notice();
        updatedNotice.setNoticeNo(existingNotice.getNoticeNo()); // 기존의 noticeNo를 설정
        updatedNotice.setNoticeTitle("새로운 제목");
        updatedNotice.setNoticeContent("새로운 내용");

        // findOne  호출될 때 existingNotice를 리턴하도록 설정
        when(noticeRepository.findByNoticeNo(updatedNotice.getNoticeNo())).thenReturn(existingNotice);

        //when
        noticeComponentService.update(updatedNotice, mockMember, multipartFiles);

        //then
        assertEquals(updatedNotice.getNoticeTitle(), existingNotice.getNoticeTitle());
        assertEquals(updatedNotice.getNoticeContent(), existingNotice.getNoticeContent());
    }


    @Test
    void testDeleteNotice() {
        // Given
        Member mockMember = new Member();
        mockMember.setMemberCategory(MemberCategory.A);
        Long noticeNo = 1L;
        Notice mockNotice = new Notice();
        mockNotice.setNoticeNo(noticeNo);
        mockNotice.setNoticeTitle("가짜 제목");
        mockNotice.setNoticeContent("가짜 내용");

        // findOne 메서드가 mockNotice를 반환하도록 설정
        when(noticeRepository.findByNoticeNo(anyLong())).thenReturn(mockNotice);

        // When
        noticeComponentService.deleteNotice(noticeNo,mockMember);

        // Then
        // mockNotice의 status가 N인지 확인
        assertEquals(NoticeStatus.N, mockNotice.getNoticeStatus());
    }

}
