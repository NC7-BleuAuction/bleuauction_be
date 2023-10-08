package bleuauction.bleuauction_be.server.item.controller;

import bleuauction.bleuauction_be.server.item.service.ItemService;
import bleuauction.bleuauction_be.server.item.web.ItemForm;
import bleuauction.bleuauction_be.server.item.entity.Item;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;
  private final EntityManager entityManager;

  //등록페이지
  @GetMapping("/item/new")
  public String createForm(Model model) {
    model.addAttribute("itemForm", new ItemForm());
    return "/items/new";
  }

  //등록 처리
  @PostMapping("/item/new")
  @Transactional
  public String item(@Valid ItemForm form) {

    Item item = new Item();
    item.setItemCode(form.getItemCode());
    item.setOriginStatus(form.getOriginStatus());
    item.setOriginPlaceStatus(form.getOriginPlaceStatus());
    item.setItemName(form.getItemName());
    item.setItemSize(form.getItemSize());
    item.setWildFarmStatus(form.getWildFarmStatus());
    item = entityManager.merge(item);
    itemService.enroll(item);
    return "redirect:/itemlist";
  }

  //품목조회
  @GetMapping("/itemlist")
  public String list(Model model) {
    List<Item> items = itemService.finditems();
    model.addAttribute("items", items);
    return "items/itemList";
  }

  //삭제
  @PostMapping("/item/delete/{itemNo}")
  public String deleteItem(@PathVariable("itemNo") Long itemNo) {
    itemService.deleteItem(itemNo);
    return "redirect:/itemlist";
  }

  //수정
  @GetMapping("/item/detail/{itemNo}")
  public String detailItem(@PathVariable("itemNo") Long itemNo, Model model) {
    Item item = itemService.findOne(itemNo);
    model.addAttribute("item", item);
    return "/items/detail";
  }

  //수정 처리
  @PostMapping("/item/update/{itemNo}")
  public String updateItem(
          @PathVariable("itemNo") Long itemNo,
          @ModelAttribute("item") @Valid ItemForm form
  ) {
    itemService.update(itemNo,form.getItemCode(),form.getOriginStatus(),form.getOriginPlaceStatus(),form.getItemName(),form.getItemSize(),form.getWildFarmStatus());
    return "redirect:/itemlist";
  }

}
