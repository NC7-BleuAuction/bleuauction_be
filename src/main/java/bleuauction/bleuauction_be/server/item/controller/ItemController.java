package bleuauction.bleuauction_be.server.item.controller;

import bleuauction.bleuauction_be.server.item.service.ItemService;
import bleuauction.bleuauction_be.server.item.web.ItemForm;
import bleuauction.bleuauction_be.server.item.entity.Item;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;
  private final EntityManager entityManager;

  //등록
  @GetMapping("/api/item/new")
  public ItemForm createForm() {
    ItemForm itemForm = new ItemForm();
    return itemForm;
  }

  //등록 처리
  @PostMapping("/api/item/new")
  @Transactional
  public ResponseEntity<String> item(@Valid @RequestBody ItemForm form) {
    Item item = new Item();
    item.setItemCode(form.getItemCode());
    item.setOriginStatus(form.getOriginStatus());
    item.setOriginPlaceStatus(form.getOriginPlaceStatus());
    item.setItemName(form.getItemName());
    item.setItemSize(form.getItemSize());
    item.setWildFarmStatus(form.getWildFarmStatus());
    item = entityManager.merge(item);
    itemService.enroll(item);

    return ResponseEntity.status(HttpStatus.CREATED).body("Item created successfully");
  }

  //품목조회
  @GetMapping(value = "/api/item", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Item> finditems() throws Exception {
    try {
      List<Item> items = itemService.finditems();
      return items;
    } catch (Exception e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  //삭제
  @PostMapping("/api/item/delete/{itemNo}")
  public ResponseEntity<String> deleteItem(@PathVariable("itemNo") Long itemNo) {
    itemService.deleteItem(itemNo);
    return ResponseEntity.ok("Item deleted successfully");
  }


  //디테일(수정)
  @GetMapping("/api/item/detail/{itemNo}")
  public ResponseEntity<Item> detailItem(@PathVariable("itemNo") Long itemNo) {
    Item item = itemService.findOne(itemNo);
    if (item != null) {
      return ResponseEntity.ok(item);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  //수정 처리
  @PostMapping("/api/item/update/{itemNo}")
  public ResponseEntity<String> updateItem(
          @PathVariable("itemNo") Long itemNo,
          @RequestBody ItemForm form
  ) {
    Item updated = itemService.update(itemNo, form.getItemCode(),form.getOriginStatus(),form.getOriginPlaceStatus(),form.getItemName(),form.getItemSize(),form.getWildFarmStatus());
    if (updated!= null) {
      return ResponseEntity.ok("Item updated successfully");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found or update failed");
    }
  }

}
