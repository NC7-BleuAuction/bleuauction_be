package bleuauction.bleuauction_be.server.store.controller;

import bleuauction.bleuauction_be.server.store.dto.StoreSignUpRequest;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.member.service.MemberService;
import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.repository.StoreRepository;
import bleuauction.bleuauction_be.server.store.service.StoreService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/store")
public class StoreController {

  @Autowired
  StoreService storeService;

  @Autowired
  MemberService memberService;

  @Autowired
  StoreRepository storeRepository;

  @GetMapping("/list")
  public String list(Model model) {
    log.info("/store/list");


    List<Store> storeList = storeService.selectStoreList();
    log.info("storeList: " + storeList);

    if (storeList != null && storeList.size() > 0) {
      List<Store> storeSubList = storeList.subList(0, storeList.size() >= 2 ? 2 : storeList.size());
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

  // 가게 회원가입
  @PostMapping("/signup")
  public Store storeSignUp(HttpSession session, @RequestBody @Valid StoreSignUpRequest request) throws Exception {
    Member loginUser = (Member) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인한 회원 정보를 찾을 수 없습니다.");
    }
    Long memberNo = loginUser.getMemberNo(); // 세션이든 뭐든 지금 로그인 되어있는 멤버의 아이디를 가져온다
    return storeRepository.save(storeService.signup(request, memberNo));
  }
}
