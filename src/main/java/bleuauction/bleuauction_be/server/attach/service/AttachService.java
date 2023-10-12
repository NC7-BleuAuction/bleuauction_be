package bleuauction.bleuauction_be.server.attach.service;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.FileStatus;
import bleuauction.bleuauction_be.server.attach.repository.AttachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AttachService {

  private final AttachRepository attachRepository;


  //수정
  public Attach update(Long fileNo) {
    Attach attach = attachRepository.findByFileNo(fileNo);
    attach.setFileStatus(FileStatus.N);
    return attach;
  }

  public List<Attach> addAttachs(ArrayList<Attach> attaches) {
    return attachRepository.saveAll(attaches);
  }
}
