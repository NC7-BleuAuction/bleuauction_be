package bleuauction.bleuauction_be.server.notice.service;

import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.notice.entity.NoticeStatus;
import bleuauction.bleuauction_be.server.notice.repository.NoticeRepository1;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Mockito의 기능을 JUnit 5 테스트에 활성화시켜 Mockito 관련 애노테이션을 사용가능
class NoticeServiceTest {

    @Mock // 가짜 객체
    private NoticeRepository1 noticeRepository;

    @InjectMocks // 가짜(Mock) 객체(@Mock로 표시된 객체)를 테스트 대상 객체에 자동으로 주입할 수 있고 이를 통해 주입된 가짜 객체를 사용하여 테스트 대상 객체의 메소드를 호출하고 동작을 검증할 수 있습니다.
    private NoticeService noticeService;

    @Test
    void testEnroll() {
        // Given
        Notice mockNotice = new Notice();
        mockNotice.setNoticeNo(1L);
        mockNotice.setNoticeTitle("가짜 제목");
        mockNotice.setNoticeContent("가짜 내용");

        // When
        Long result = noticeService.enroll(mockNotice);

        // Then
        // 반환값이 mockNotice의 noticeNo와 일치하는지 확인
        assertEquals(mockNotice.getNoticeNo(), result);
    }

    @Test
    @DisplayName("노티스 수정")
    void testUpdateNotice() {

        //given
        Notice existingNotice = new Notice();
        existingNotice.setNoticeNo(1L);
        existingNotice.setNoticeTitle("기존 제목");
        existingNotice.setNoticeContent("기존 내용");

        // findOne  호출될 때 existingNotice를 리턴하도록 설정
        when(noticeRepository.findByNoticeNo(existingNotice.getNoticeNo())).thenReturn(existingNotice);

        // 업데이트할 내용을 담은 새로운 Notice 객체 생성
        Notice updatedNotice = new Notice();
        updatedNotice.setNoticeNo(existingNotice.getNoticeNo()); // 기존의 noticeNo를 설정
        updatedNotice.setNoticeTitle("새로운 제목");
        updatedNotice.setNoticeContent("새로운 내용");

        //when
        // 서비스의 update 메소드 호출
        Notice result = noticeService.update(updatedNotice);

        //then
        // 업데이트된 내용이 반영되었는지 확인
        assertEquals(updatedNotice.getNoticeTitle(), result.getNoticeTitle());
        assertEquals(updatedNotice.getNoticeContent(), result.getNoticeContent());
    }


    @Test
    void testDeleteNotice() {
        // Given
        Long noticeNo = 1L;
        Notice mockNotice = new Notice();
        mockNotice.setNoticeNo(noticeNo);
        mockNotice.setNoticeTitle("가짜 제목");
        mockNotice.setNoticeContent("가짜 내용");

        // findOne 메서드가 mockNotice를 반환하도록 설정
        when(noticeRepository.findByNoticeNo(anyLong())).thenReturn(mockNotice);

        // When
        noticeService.deleteNotice(noticeNo);

        // Then
        // mockNotice의 status가 N인지 확인
        assertEquals(NoticeStatus.N, mockNotice.getNoticeStatus());
    }

}
