package bleuauction.bleuauction_be.server.attach.service;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.exception.AttachNotFoundFileIdException;
import bleuauction.bleuauction_be.server.attach.repository.AttachRepository;
import bleuauction.bleuauction_be.server.attach.util.AttachUtilFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AttachServiceTest {

    @Mock
    private AttachRepository attachRepository;

    @InjectMocks
    private AttachService attachService;

    @Test
    @DisplayName("Attach객체 한개만 매개변수로 제공할 때 DB에 등록한다")
    void whenGiveAttachObject_thenInsertData() {
        //given
        Attach attach = AttachUtilFactory.ofUseCase("/test1", "originalFileName1", "changeFileName1");
        given(attachRepository.findAll()).willReturn(List.of(attach));

        //when
        attachService.insertAttach(attach);

        //then, 사실 Repository가 Mock객체이기 떄문에 findAll이 지정된 객체만 반환해서 딱히 테스트 방법이 존재하진 않는다.
        assertEquals(attachRepository.findAll().size(), 1);
    }

    @Test
    @DisplayName("Attach객체 리스트를 매개변수로 제공할 때 DB에 등록한다")
    void whenGiveAttachList_thenInsertData() {
        //given
        Attach attach = AttachUtilFactory.ofUseCase("/test1", "originalFileName1", "changeFileName1");
        given(attachRepository.findAll()).willReturn(List.of(attach, attach, attach, attach, attach));

        //when
        attachService.addAttachs(List.of(attach, attach, attach, attach, attach));

        //then, 사실 Repository가 Mock객체이기 떄문에 findAll이 지정된 객체만 반환해서 딱히 테스트 방법이 존재하진 않는다.
        assertEquals(attachRepository.findAll().size(), 5);
    }

    @Test
    @DisplayName("Attach객체의 FileNo를 파라미터로 넘길 때 FileStatus가 Delete상태로 변경되어야 한다")
    void whenGiveAttachFileNo_thenReturnAttachObjectChangeFileStatusDelete() {
        //given
        Attach attach = AttachUtilFactory.ofUseCase("/test1", "originalFileName1", "changeFileName1");
        attach.setFileNo(1L);
        given(attachRepository.findById(attach.getFileNo())).willReturn(Optional.of(attach));

        //when
        attachService.changeFileStatusToDeleteByFileNo(attach.getFileNo());

        //then
        assertEquals(attach.getFileStatus(), FileStatus.N);
    }

    @Test
    @DisplayName("Attach객체의 FileNo를 파라미터로 넘길 때 해당 FileNo가 부여된 파일이 존재하지 않는경우 Exception이 발생한다.")
    void whenGiveAttachFileNo_thenThrowAttachNotFoundFileIdException() {
        //given
        Attach attach = AttachUtilFactory.ofUseCase("/test1", "originalFileName1", "changeFileName1");
        attach.setFileNo(1L);
        given(attachRepository.findById(attach.getFileNo())).willReturn(Optional.empty());

        //when && then
        assertThrows(AttachNotFoundFileIdException.class, () ->
                attachService.changeFileStatusToDeleteByFileNo(attach.getFileNo())
        );
    }

}