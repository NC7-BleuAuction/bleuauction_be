package bleuauction.bleuauction_be.server.attach.Controller;

import bleuauction.bleuauction_be.server.attach.service.AttachService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class AttachController {

    private final AttachService attachService;

    /**
     * fileNo를 제공할 경우 해당 fileNo를 가진 파일의 상태를 삭제상태로 변경한다.
     * @param fileNo
     * @return
     */
    @DeleteMapping("/{fileNo}")
    public ResponseEntity<String> fileDelete(@PathVariable Long fileNo) {
        attachService.changeFileStatusToDeleteByFileNo(fileNo);
        return ResponseEntity.ok("File deleted successfully");
    }

}
