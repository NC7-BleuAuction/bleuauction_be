package bleuauction.bleuauction_be.server.store.controller;

import bleuauction.bleuauction_be.server.store.entity.Store;
import bleuauction.bleuauction_be.server.store.service.StoreService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/store")
public class StoreController {

  @Autowired
  StoreService storeService;

  @GetMapping("/list")
  public String list(Model model) {
    log.info("/store/list");

    List<Store> storeList = storeService.selectStoreList();

    List<Store> storeSubList = storeList.subList(0, 2);

    log.info("storeList: " + storeList);
    log.info("storeSubList: " + storeSubList);

    model.addAttribute("storeListSize", storeList.size());
    model.addAttribute("storeSubList", storeSubList);


//        return "/store/list GET 요청 Test: " + storeList;
    return "testStoreList";
  }

  @ResponseBody
  @GetMapping("/ajaxList")
  public List<Store> ajaxList(@RequestParam(name = "storeLength", defaultValue = "0") int storeLength) {
    log.info("/store/ajaxList");
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
  public String detail(Model model, @RequestParam(name = "storeNo", defaultValue = "1") Long storeNo) {
    log.info("/store/detail");
    Store store = storeService.selectStore(storeNo);
    log.info("storeNo가 " + storeNo + "인 가게" + store);

    model.addAttribute("store", store);

    return "testStoreDetail";
  }


}
