package bleuauction.bleuauction_be.server.notice.service;//package bleuauction.bleuauction_be.server.notice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.AttachVO;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.entity.NoticeAttach;
import bleuauction.bleuauction_be.server.attach.repository.AttachRepository;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.notice.dto.NoticeDTO;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.notice.entity.NoticeStatus;
import bleuauction.bleuauction_be.server.notice.repository.NoticeRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class) // Mockito의 기능을 JUnit 5 테스트에 활성화시켜 Mockito 관련 애노테이션을 사용가능
class NoticeComponentServiceTest {

    @Mock AttachRepository attachRepository;
    @Mock NoticeModuleService noticeModuleService;
    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private NoticeComponentService noticeComponentService;


    @Test
    void testEnroll() {
        // Given
        Member mockMember = Member.builder().build();
        mockMember.setCategory(MemberCategory.A);

        Notice mockNotice = Notice.builder().member(mockMember).build();
        mockNotice.setId(100L);

        List<MultipartFile> multipartFiles = new ArrayList<>();

        // When
        when(noticeModuleService.save(any(Notice.class))).thenReturn(mockNotice);
        Long result = noticeComponentService.enroll(mockNotice, multipartFiles, mockMember);

        // Then
        assertEquals(mockNotice.getId(), result);
    }


    @Test
    @DisplayName("노티스 수정")
    void testUpdateNotice() throws Exception {

        // given
        Member mockMember = Member.builder().build();
        mockMember.setCategory(MemberCategory.A);

        Notice existingNotice = Notice.builder().build();
        existingNotice.setId(100L);
        List<MultipartFile> multipartFiles = new ArrayList<>();

        Attach attach1 =
                new NoticeAttach(
                        AttachVO.builder()
                                .filePath("FilePath")
                                .originFilename("originFilename")
                                .saveFileName("saveFilename")
                                .fileStatus(FileStatus.Y)
                                .build(),
                        existingNotice);
        attachRepository.save(attach1);

        existingNotice.setNoticeTitle("기존 제목");
        existingNotice.setNoticeContent("기존 내용");
        noticeRepository.save(existingNotice);

        NoticeDTO request = NoticeDTO.builder().build();
        request.setNoticeContent("update content");
        request.setNoticeTitle("update title");

        // findOne  호출될 때 existingNotice를 리턴하도록 설정
        when(noticeModuleService.findOne(existingNotice.getId())).thenReturn(existingNotice);

        // when
        noticeComponentService.update(100L, mockMember, multipartFiles, request);

        // then
        assertEquals(request.getNoticeTitle(), existingNotice.getNoticeTitle());
        assertEquals(request.getNoticeContent(), existingNotice.getNoticeContent());
    }

    @Test
    void testDeleteNotice() {
        // Given
        Member mockMember = Member.builder().build();
        mockMember.setCategory(MemberCategory.A);
        Long noticeNo = 1L;
        Notice mockNotice = Notice.builder().build();
        mockNotice.setId(noticeNo);
        mockNotice.setNoticeTitle("가짜 제목");
        mockNotice.setNoticeContent("가짜 내용");

        // findOne 메서드가 mockNotice를 반환하도록 설정
        when(noticeModuleService.findOne(noticeNo)).thenReturn(mockNotice);

        // When
        noticeComponentService.deleteNotice(noticeNo, mockMember);

        // Then
        // mockNotice의 status가 N인지 확인
        assertEquals(NoticeStatus.N, mockNotice.getNoticeStatus());
    }
}
