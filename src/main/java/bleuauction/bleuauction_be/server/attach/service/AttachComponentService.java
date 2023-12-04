package bleuauction.bleuauction_be.server.attach.service;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.config.annotation.ComponentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Transactional을 사용하지 않는 경우로 변경함.(Dirty Checking을 사용하지 않는 경우)
 */
@Slf4j
@ComponentService
@RequiredArgsConstructor
public class AttachComponentService {
    private final AttachModuleService attachModuleService;

    /**
     * FileId(FileNo)를 매개변수르 받아 파일의 상태를 조회후 존재할 경우 FileStatus를 N으로 업데이트한다. <br />
     * 존재하지 않는 fileNo인 경우에는 AttachNotFoundFileIdException가 발생한다.
     * @param fileNo
     * @return
     */
    public Attach changeFileStatusDeleteByFileNo(Long fileNo) {
        Attach target = attachModuleService.findById(fileNo);
        target.changeFileStatusToDelete();
        return attachModuleService.save(target);
    }

}
