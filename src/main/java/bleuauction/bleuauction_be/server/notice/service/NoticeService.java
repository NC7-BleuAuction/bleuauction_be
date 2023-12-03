package bleuauction.bleuauction_be.server.notice.service;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.notice.entity.NoticeStatus;
import bleuauction.bleuauction_be.server.notice.repository.NoticeRepository1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static bleuauction.bleuauction_be.server.member.entity.MemberCategory.A;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

  private final NoticeRepository1 noticeRepository;
  private final NcpObjectStorageService ncpObjectStorageService;
  private final AttachService attachService;

  @Transactional
  public Long enroll(Notice notice, List<MultipartFile> multipartFiles, Member member) {

    if(member.getMemberCategory() == MemberCategory.A) {
      notice.setMemberNo(member);
      noticeRepository.save(notice);

      if (multipartFiles != null && multipartFiles.size() > 0) {
        ArrayList<Attach> attaches = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
          if (multipartFile.getSize() > 0) {
            Attach attach = ncpObjectStorageService.uploadFile(new Attach(),
                    "bleuauction-bucket", "notice/", multipartFile);
            notice.addNoticeAttach(attach);
          }
        }
      }

    }
    return notice.getNoticeNo();

  }

  //노티스 전체 조회
  @Transactional(readOnly = true)
  public List<Notice> findNotices() {
    return noticeRepository.findAll();
  }

  public List<Notice> findNoticesByStatus(NoticeStatus status) {
    return noticeRepository.findByNoticeStatus(status);
  }


  @Transactional(readOnly = true)
  public Notice findOne(Long noticeNo) {
    return noticeRepository.findByNoticeNo(noticeNo);
  }


  //노티스 삭제(N)
  @Transactional
  public void deleteNotice(Long noticeNo, Member member) {
    if (member.getMemberCategory()==MemberCategory.A) {
      Notice notice = noticeRepository.findByNoticeNo(noticeNo);
      notice.delete();
      if (notice.getNoticeAttaches() != null && !notice.getNoticeAttaches().isEmpty()) {
        for (Attach attach : notice.getNoticeAttaches()) {
          attachService.changeFileStatusToDeleteByFileNo(attach.getFileNo());
        }
      }
    }
  }

  //노티스 수정
  @Transactional
  public Notice update(Notice updatedNotice, Member member, List<MultipartFile> multipartFiles) throws Exception{

    Notice existingnotice = noticeRepository.findByNoticeNo(updatedNotice.getNoticeNo());

    if(member.getMemberCategory() == A) {

      existingnotice.setNoticeTitle(updatedNotice.getNoticeTitle());
      existingnotice.setNoticeContent(updatedNotice.getNoticeContent());

      if (multipartFiles != null && multipartFiles.size() > 0) {
        for (MultipartFile multipartFile : multipartFiles) {
          if (multipartFile.getSize() > 0) {
            Attach attach = ncpObjectStorageService.uploadFile(new Attach(),
                    "bleuauction-bucket", "notice/", multipartFile);
            existingnotice.addNoticeAttach(attach);
          }
        }
      }
      return noticeRepository.save(existingnotice);
    } else {
      throw new IllegalArgumentException("수정 권한이 없습니다.");
    }

  }



}
