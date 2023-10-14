package bleuauction.bleuauction_be.server.store.controller;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.member.dto.UpdateMemberRequest;
import bleuauction.bleuauction_be.server.member.entity.MemberStatus;
import bleuauction.bleuauction_be.server.member.exception.MemberNotFoundException;
import bleuauction.bleuauction_be.server.member.repository.MemberRepository;
import bleuauction.bleuauction_be.server.member.service.UpdateMemberService;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import bleuauction.bleuauction_be.server.store.dto.StoreSignUpRequest;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.service.MemberService;
import bleuauction.bleuauction_be.server.store.dto.UpdateStoreRequest;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.entity.StoreStatus;
import bleuauction.bleuauction_be.server.store.exception.StoreNotFoundException;
import bleuauction.bleuauction_be.server.store.repository.StoreRepository;
import bleuauction.bleuauction_be.server.store.service.StoreService;
import bleuauction.bleuauction_be.server.store.service.UpdateStoreService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/store")
public class StoreController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    StoreService storeService;

    @Autowired
    MemberService memberService;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    NcpObjectStorageService ncpObjectStorageService;

    @Autowired
    AttachService attachService;

    @GetMapping("/list")
    public String list(Model model) {
        log.info("/store/list");

        List<Store> storeList = storeService.selectStoreList();
        log.info("storeList: " + storeList);

        if (storeList != null && storeList.size() > 0) {
            List<Store> storeSubList = storeList.subList(0,
                    storeList.size() >= 2 ? 2 : storeList.size());
            log.info("storeSubList: " + storeSubList);

            model.addAttribute("storeListSize", storeList.size());
            model.addAttribute("storeSubList", storeSubList);
        }

//        return "/store/list GET 요청 Test: " + storeList;
        return "testStoreList";
    }

  @ResponseBody
  @GetMapping("/list/axios")
  public List<Store> listAxios(@RequestParam(name = "storeLength", defaultValue = "0") int storeLength) {
    log.info("/store/list/axios");
    log.info("storeLength: " + storeLength);

    List<Store> storeList = storeService.selectStoreList();
    log.info("storeList: " + storeList);
    log.info("storeList.size(): " + storeList.size());

    List<Store> storeSubList = null;
    if (storeLength > storeList.size()) {
      storeSubList = storeList.subList(storeLength - 2, storeList.size());
    } else {
      storeSubList = storeList.subList(storeLength - 2, storeLength);
    }

    log.info("storeSubList: " + storeSubList);

    return storeSubList;
  }

    @GetMapping("/detail")
    public String detail(Model model,
            @RequestParam(name = "storeNo", defaultValue = "1") Long storeNo) {
        log.info("/store/detail");
        Optional<Store> store = storeService.selectStore(storeNo);
        log.info("storeNo가 " + storeNo + "인 가게" + store);

        if (store.isPresent()) {
            model.addAttribute("store", store);
        }
        return "testStoreDetail";
    }

    // 가게 등록
    @PostMapping("/signup")
    public Store storeSignUp(HttpSession session, @RequestBody @Valid StoreSignUpRequest request)
            throws Exception {
        Member loginUser = (Member) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new Exception("로그인한 회원 정보를 찾을 수 없습니다.");
        }
        Long memberNo = loginUser.getMemberNo(); // 지금 로그인 되어있는 멤버의 아이디를 가져온다
        return storeRepository.save(storeService.signup(request, memberNo));
    }

    // 가게정보수정
    @PutMapping("/update")
    public ResponseEntity<String> updateStore(HttpSession session,
            @RequestPart("updateStoreRequest") UpdateStoreRequest updateStoreRequest,
            @RequestPart("profileImage") MultipartFile profileImage)
            throws Exception {
        Member loginUser = (Member) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        try {
            UpdateStoreService updateStoreService = new UpdateStoreService(storeRepository);
            // 첨부 파일 목록 추가
            List<Attach> attaches = new ArrayList<>();
            if (profileImage != null) {
                log.info("첨부 파일 이름: {}", profileImage.getOriginalFilename());
                if (profileImage.getSize() > 0) {
                    Attach attach = ncpObjectStorageService.uploadFile(new Attach(),
                            "bleuauction-bucket", "store/", profileImage);
                    attach.setMemberNo(loginUser);
                    attaches.add(attach);
                }
            }
            // 첨부 파일 저장 및 결과를 insertAttaches에 할당
            ArrayList<Attach> insertAttaches = (ArrayList<Attach>) attachService.addAttachs(
                    (ArrayList<Attach>) attaches);
            // 가게 정보 업데이트
            updateStoreService.updateStore(loginUser.getMemberNo(), updateStoreRequest);
            log.info("가게 정보가 업데이트되었습니다. 업데이트된 가게 정보: {}", updateStoreRequest);
            return ResponseEntity.ok("가게 정보가 업데이트되었습니다.");
        } catch (StoreNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("가게를 찾을 수 없습니다.");
        }
    }
    // 가게 탈퇴
    @PutMapping("/withdraw/{storeNo}")
    public ResponseEntity<String> withdrawStore(HttpSession session, @PathVariable("storeNo") Long storeNo) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        Optional<Store> storeOptional = storeService.selectStore(storeNo);

        // 체크: storeOptional이 비어있는지 확인
        if (!storeOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("가게를 찾을 수 없습니다.");
        }
        Store store = storeOptional.get();  // Store 객체 가져오기

        if (store.getMemberNo() == loginUser.getMemberNo()) {

            // 가게 상태를 'N'으로 변경하여 탈퇴 처리
            store.setStoreStatus(StoreStatus.N);
            storeRepository.save(store);
            // 세션을 무효화하여 로그아웃 처리
            session.invalidate();

                log.info("가게가 성공적으로 폐업되었습니다. 가게번호: {}", loginUser.getMemberNo());
                return ResponseEntity.ok("가게가 성공적으로 폐업되었습니다.");
            } else {
                log.error("가게정보는: {}", ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("올바른 가게 정보가 아닙니다.");
        }
    }

    // 가게 프로필 삭제
    @DeleteMapping("/delete/profileImage/{fileNo}")
    public ResponseEntity<String> deleteProfileImage(@PathVariable Long fileNo) {
        Attach attach = attachService.getProfileImageByFileNo(fileNo);
        if (attach == null) {
            return new ResponseEntity<>("첨부파일을 찾을 수 없습니다", HttpStatus.NOT_FOUND);
        }
        boolean isDeleted = attachService.deleteProfileImage(attach);
        if (isDeleted) {
            return new ResponseEntity<>("첨부파일이 성공적으로 삭제되었습니다", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("첨부파일 삭제에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
