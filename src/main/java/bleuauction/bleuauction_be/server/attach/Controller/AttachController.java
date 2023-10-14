package bleuauction.bleuauction_be.server.attach.Controller;

import bleuauction.bleuauction_be.server.attach.service.AttachService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AttachController {

  private final AttachService attachService;

  //사진삭제
  @DeleteMapping("/api/deletefile/{fileNo}")
  public ResponseEntity<String> fileDelete(@PathVariable Long fileNo) {
    attachService.update(fileNo);
    return ResponseEntity.ok("File deleted successfully");
  }

}
