package bleuauction.bleuauction_be.server.attach.service;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.repository.AttachRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AttachService {

  private final AttachRepository attachRepository;

  //수정(삭제)
  public Attach update(Long fileNo) {
    Attach attach = attachRepository.findByFileNo(fileNo);
    attach.setFileStatus(FileStatus.N);
    return attach;
  }
  //삭제
  public boolean deleteProfileImage(Attach attach) {
    try {
      // 파일 삭제 관련 로직을 구현
      attach.setFileStatus(FileStatus.N);
      attachRepository.save(attach);
      return true;
    } catch (Exception e) {
      // 파일 삭제 실패 시 예외 처리
      e.printStackTrace();
      return false;
    }
  }

  public Attach getProfileImageByFileNo(Long fileNo) {
    return attachRepository.findByFileNo(fileNo);
  }

  public List<Attach> addAttachs(ArrayList<Attach> attaches) {
    return attachRepository.saveAll(attaches);
  }
}
