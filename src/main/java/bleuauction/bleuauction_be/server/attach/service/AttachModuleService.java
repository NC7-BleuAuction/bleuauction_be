package bleuauction.bleuauction_be.server.attach.service;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.exception.AttachNotFoundFileIdException;
import bleuauction.bleuauction_be.server.attach.repository.AttachRepository;
import bleuauction.bleuauction_be.server.config.annotation.ModuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@ModuleService
@Transactional
@RequiredArgsConstructor
public class AttachModuleService {
    private final AttachRepository attachRepository;

    /**
     * FiltNo(Id)값을 기반으로 Attach객체를 획득한다.
     * @param fileNo
     * @return
     */
    public Attach findById(Long fileNo) {
        return attachRepository.findById(fileNo).orElseThrow(()-> new AttachNotFoundFileIdException(fileNo));
    }

    /**
     * FileNo를 기반으로 존재유무 확인
     * @param fileNo
     * @return
     */
    public boolean isExistsByFileNo(Long fileNo) {
        return attachRepository.existsById(fileNo);
    }

    /**
     * Attach List 저장 및 리스트 반환
     * @param attaches
     * @return
     */
    public List<Attach> saveAll(List<Attach> attaches) {
        return attachRepository.saveAll(attaches);
    }

    /**
     * 단건의 파일에 대하여 Insert한다.
     * @param attach
     * @return
     */
    public Attach save(Attach attach) {
        return attachRepository.save(attach);
    }

}
