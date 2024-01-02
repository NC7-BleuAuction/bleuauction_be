package bleuauction.bleuauction_be.server.notice.service;

import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.notice.repository.NoticeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoticeModuleServiceTest {
    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private NoticeModuleService noticeModuleService;

    @Test
    void testFindOne() {
        // Given
        Long noticeNo = 1L;
        Notice mockNotice = Notice.builder().build();
        mockNotice.setId(noticeNo);

        // When
        when(noticeRepository.findById(noticeNo)).thenReturn(Optional.ofNullable(mockNotice));
        Notice result = noticeModuleService.findOne(noticeNo);

        // Then
        assertNotNull(result);
        assertEquals(noticeNo, result.getId());
    }


}