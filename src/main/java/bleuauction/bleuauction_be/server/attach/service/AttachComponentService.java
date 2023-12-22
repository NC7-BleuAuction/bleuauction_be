package bleuauction.bleuauction_be.server.attach.service;


import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.MemberAttach;
import bleuauction.bleuauction_be.server.attach.entity.MenuAttach;
import bleuauction.bleuauction_be.server.attach.entity.NoticeAttach;
import bleuauction.bleuauction_be.server.attach.entity.ReviewAttach;
import bleuauction.bleuauction_be.server.attach.entity.StoreAttach;
import bleuauction.bleuauction_be.server.attach.exception.AttachUploadBadRequestException;
import bleuauction.bleuauction_be.server.attach.type.FileUploadUsage;
import bleuauction.bleuauction_be.server.attach.util.NcpObjectStorageUtil;
import bleuauction.bleuauction_be.server.config.annotation.ComponentService;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.notice.entity.Notice;
import bleuauction.bleuauction_be.server.review.entity.Review;
import bleuauction.bleuauction_be.server.store.entity.Store;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/** Transactional을 사용하지 않는 경우로 변경함.(Dirty Checking을 사용하지 않는 경우) */
@Slf4j
@ComponentService
@RequiredArgsConstructor
public class AttachComponentService {
    private final AttachModuleService attachModuleService;
    private final NcpObjectStorageUtil ncpObjectStorageUtil;

    /**
     * FileId(FileNo)를 매개변수르 받아 파일의 상태를 조회후 존재할 경우 FileStatus를 N으로 업데이트한다. <br>
     * 존재하지 않는 fileNo인 경우에는 AttachNotFoundFileIdException가 발생한다.
     *
     * @param fileNo
     * @return
     */
    public Attach changeFileStatusDeleteByFileNo(Long fileNo) {
        Attach target = attachModuleService.findById(fileNo);
        target.deleteAttach();
        return attachModuleService.save(target);
    }

    /**
     * 파일을 업로드한 뒤, Attach를 생성하며, Member에 Attach를 추가한뒤 해당 Attach를 반환한다.
     *
     * @param member Member를 저장할 객체
     * @param usage 사용처
     * @param parts Object Storage에 저장을 하고자 하는 파일
     * @return
     */
    public Attach saveWithMember(Member member, FileUploadUsage usage, MultipartFile parts) {
        isObjEmpty(member);
        return attachModuleService.save(
                new MemberAttach(ncpObjectStorageUtil.uploadFile(usage, parts), member));
    }

    /**
     * 파일을 업로드한 뒤, Attach를 생성하며, Menu에 Attach를 추가한뒤 해당 Attach를 반환한다.
     *
     * @param menu Menu를 저장할 객체
     * @param usage 사용처
     * @param parts Object Storage에 저장을 하고자 하는 파일
     * @return
     */
    public Attach saveWithMenu(Menu menu, FileUploadUsage usage, MultipartFile parts) {
        isObjEmpty(menu);
        return attachModuleService.save(
                new MenuAttach(ncpObjectStorageUtil.uploadFile(usage, parts), menu));
    }

    /**
     * 파일을 업로드한 뒤, Attach를 생성하며, Review에 Attach를 추가한뒤 해당 Attach를 반환한다.
     *
     * @param review Review를 저장할 객체
     * @param usage 사용처
     * @param parts Object Storage에 저장을 하고자 하는 파일
     * @return
     */
    public Attach saveWithReview(Review review, FileUploadUsage usage, MultipartFile parts) {
        isObjEmpty(review);
        return attachModuleService.save(
                new ReviewAttach(ncpObjectStorageUtil.uploadFile(usage, parts), review));
    }

    /**
     * 파일을 업로드한 뒤, Attach를 생성하며, Notice에 Attach를 추가한뒤 해당 Attach를 반환한다.
     *
     * @param notice Notice를 저장할 객체
     * @param usage 사용처
     * @param parts Object Storage에 저장을 하고자 하는 파일
     * @return
     */
    public Attach saveWithNotice(Notice notice, FileUploadUsage usage, MultipartFile parts) {
        isObjEmpty(notice);
        return attachModuleService.save(
                new NoticeAttach(ncpObjectStorageUtil.uploadFile(usage, parts), notice));
    }

    /**
     * 파일을 업로드한 뒤, Attach를 생성하며, Store에 Attach를 추가한뒤 해당 Attach를 반환한다.
     *
     * @param store Store를 저장할 객체
     * @param usage 사용처
     * @param parts Object Storage에 저장을 하고자 하는 파일
     * @return
     */
    public Attach saveWithStore(Store store, FileUploadUsage usage, MultipartFile parts) {
        isObjEmpty(store);
        return attachModuleService.save(
                new StoreAttach(ncpObjectStorageUtil.uploadFile(usage, parts), store));
    }

    /**
     * 제공받은 Entity가 Null인지 확인을 하고자 하는 경우
     *
     * @param obj
     */
    private void isObjEmpty(Object obj) {
        if (obj == null) {
            throw new AttachUploadBadRequestException();
        }
    }
}
