package bleuauction.bleuauction_be.server.attach.service;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.exception.AttachNotFoundFileIdException;
import bleuauction.bleuauction_be.server.attach.repository.AttachRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Transactional을 사용하지 않는 경우로 변경함.(Dirty Checking을 사용하지 않는 경우)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AttachService {

    private final AttachRepository attachRepository;

    /**
     * FileId(FileNo)를 매개변수르 받아 파일의 상태를 조회후 존재할 경우 FileStatus를 N으로 업데이트한다.
     * @param fileNo
     * @return
     */
    public Attach changeFileStatusToDeleteByFileNo(Long fileNo) {
        return changeFileStatusToDeleteAndSaveByAttach(findAttachByFileNo(fileNo));
    }

    //삭제
    public boolean changeProfileImageToDeleteByAttachEntity(Attach attach) {
        try {
            // 파일 삭제 관련 로직을 구현
            changeFileStatusToDeleteAndSaveByAttach(attach);
            return true;
        } catch (Exception e) {
            // 파일 삭제 실패 시 예외 처리
            log.error("[{}] {} >>> 삭제처리 진행도중 예외 발생, Attach ID : {}", this.getClass().getName(), "changeProfileImageToDeleteByAttachEntity", String.valueOf(attach.getFileNo()));
            return false;
        }
    }

    public Attach getProfileImageByFileNo(Long fileNo) {
        return findAttachByFileNo(fileNo);
    }

    public void addAttachs(List<Attach> attaches) {
        attachRepository.saveAll(attaches);
    }

    /**
     * 단건의 파일에 대하여 Insert한다.
     * @param attach
     * @return
     */
    public void insertAttach(Attach attach) {
        attachRepository.save(attach);
    }

    /**
     * FiltNo(Id)값을 기반으로 Attach객체를 획득한다.
     * @param fileNo
     * @return
     */
    private Attach findAttachByFileNo(Long fileNo) {
        return attachRepository.findById(fileNo).orElseThrow(()-> new AttachNotFoundFileIdException(fileNo));
    }

    /**
     *
     * @param attach
     * @return
     */
    private Attach changeFileStatusToDeleteAndSaveByAttach(Attach attach) {
        attach.changeFileStatusToDelete();
        attachRepository.save(attach);
        return attach;
    }
}
