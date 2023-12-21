package bleuauction.bleuauction_be.server.notice.service;

import bleuauction.bleuauction_be.server.attach.service.AttachComponentService;
import bleuauction.bleuauction_be.server.attach.type.FileUploadUsage;
import bleuauction.bleuauction_be.server.config.annotation.ComponentService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.entity.MemberCategory;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@ComponentService
@Transactional
@RequiredArgsConstructor
public class NoticeComponentService {

    private final NoticeModuleService noticeModuleService;
    private final NoticeRepository noticeRepository;
    private final AttachComponentService attachComponentService;


    @Transactional
    public Long enroll(Notice notice, List<MultipartFile> multipartFiles, Member member) {

        isMemberAdmin(member.getMemberCategory());
        notice.setMemberNo(member);

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            multipartFiles.stream()
                    .filter(file -> file.getSize() > 0)
                    .forEach(multipartFile ->
                            attachComponentService.saveWithNotice(notice, FileUploadUsage.NOTICE, multipartFile)
                    );
        }
        return noticeModuleService.save(notice).getId();

    }

    //노티스 삭제(N)
    @Transactional
    public void deleteNotice(Long noticeNo, Member member) {
        isMemberAdmin(member.getMemberCategory());

        Notice notice = noticeRepository.findByNoticeNo(noticeNo);
        notice.delete();
        if (notice.getAttaches() != null && !notice.getAttaches().isEmpty()) {
            notice.getAttaches().forEach(attach -> attachComponentService.changeFileStatusDeleteByFileNo(attach.getId()));
        }
    }

    //노티스 수정
    @Transactional
    public Notice update(long noticeNo, Member member, List<MultipartFile> multipartFiles) throws Exception{

        Notice updatedNotice = noticeModuleService.findOne(noticeNo);
        Notice existingnotice = noticeRepository.findByNoticeNo(noticeNo);

        isMemberAdmin(member.getMemberCategory());

        existingnotice.setNoticeTitle(updatedNotice.getNoticeTitle());
        existingnotice.setNoticeContent(updatedNotice.getNoticeContent());

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            multipartFiles.stream()
                    .filter(file -> file.getSize() > 0)
                    .forEach(multipartFile ->
                            attachComponentService.saveWithNotice(existingnotice, FileUploadUsage.NOTICE, multipartFile)
                    );
        }

        return noticeModuleService.save(existingnotice);


    }

    private void isMemberAdmin(MemberCategory memberCategory) {
        if (!MemberCategory.A.equals(memberCategory)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

}


