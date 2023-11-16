package bleuauction.bleuauction_be.server.attach.service;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.repository.AttachRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Transactional을 사용하지 않는 경우로 변경함.(Dirty Checking을 사용하지 않는 경우)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AttachService {

    private final AttachRepository attachRepository;

    //수정(삭제)
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

    public List<Attach> addAttachs(ArrayList<Attach> attaches) {
        return attachRepository.saveAll(attaches);
    }

    /**
     * 단건의 파일에 대하여 Insert한다.
     * @param attach
     * @return
     */
    public Attach insertAttach(Attach attach) {
        return attachRepository.save(attach);
    }

    /**
     * FiltNo(Id)값을 기반으로 Attach객체를 획득한다.
     * @param fileNo
     * @return
     */
    private Attach findAttachByFileNo(Long fileNo) {
        return attachRepository.findById(fileNo).orElseThrow(()-> new RuntimeException("No Such File"));
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
