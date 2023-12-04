package bleuauction.bleuauction_be.server.attach.service;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.exception.AttachNotFoundFileIdException;
import bleuauction.bleuauction_be.server.attach.util.AttachUtilFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AttachComponentServiceTest {

    @Mock
    private AttachModuleService attachModuleService;

    @InjectMocks
    private AttachComponentService attachComponentService;

    @Test
    @DisplayName("Attach객체의 FileNo를 파라미터로 넘길 때 FileStatus가 Delete상태로 변경되어야 한다")
    void whenGiveAttachFileNo_thenReturnAttachObjectChangeFileStatusDelete() {
        //given
        Attach attach = AttachUtilFactory.ofUseCase("/test1", "originalFileName1", "changeFileName1");
        attach.setFileNo(1L);
        given(attachModuleService.findById(attach.getFileNo())).willReturn(attach);

        //when
        attachComponentService.changeFileStatusDeleteByFileNo(attach.getFileNo());

        //then
        assertEquals(attach.getFileStatus(), FileStatus.N);
    }

    @Test
    @DisplayName("Attach객체의 FileNo를 파라미터로 넘길 때 해당 FileNo가 부여된 파일이 존재하지 않는경우 Exception이 발생한다.")
    void whenGiveAttachFileNo_thenThrowAttachNotFoundFileIdException() {
        //given
        Attach attach = AttachUtilFactory.ofUseCase("/test1", "originalFileName1", "changeFileName1");
        attach.setFileNo(1L);
        given(attachModuleService.findById(attach.getFileNo())).willThrow(new AttachNotFoundFileIdException(attach.getFileNo()));

        //when && then
        assertThrows(AttachNotFoundFileIdException.class, () ->
                attachComponentService.changeFileStatusDeleteByFileNo(attach.getFileNo())
        );
    }

}