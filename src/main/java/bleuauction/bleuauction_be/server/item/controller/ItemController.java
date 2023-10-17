package bleuauction.bleuauction_be.server.item.controller;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.service.AttachService;
import bleuauction.bleuauction_be.server.item.service.ItemService;
import bleuauction.bleuauction_be.server.item.web.ItemForm;
import bleuauction.bleuauction_be.server.item.entity.Item;
import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.ncp.NcpObjectStorageService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static bleuauction.bleuauction_be.server.member.entity.MemberCategory.A;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;
  private final EntityManager entityManager;
  private final NcpObjectStorageService ncpObjectStorageService;
  private final AttachService attachService;

  //등록
  @GetMapping("/api/item/new")
  public ItemForm createForm() {
    ItemForm itemForm = new ItemForm();
    return itemForm;
  }

  //등록 처리
  @PostMapping("/api/item/new")
  @Transactional
  public ResponseEntity<String> item(HttpSession session, Item item, @RequestParam(name = "multipartFiles",required = false) List<MultipartFile> multipartFiles) {

    Member loginUser = (Member) session.getAttribute("loginUser");

    if(loginUser.getMemberCategory() == A) {
      item.setMemberNo(loginUser);

      if (multipartFiles != null && multipartFiles.size() > 0) {
        ArrayList<Attach> attaches = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
          if (multipartFile.getSize() > 0) {
            Attach attach = ncpObjectStorageService.uploadFile(new Attach(),
                    "bleuauction-bucket", "item/", multipartFile);
            item.addItemAttach(attach);
          }
        }
      }

      item = entityManager.merge(item);
      itemService.enroll(item);

      log.info("item/postnew");
      return ResponseEntity.status(HttpStatus.CREATED).body("Item created successfully");
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("관리자 권한이 필요합니다");
    }
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
  public ResponseEntity<String> deleteItem(HttpSession session, @PathVariable("itemNo") Long itemNo) {
    Item item = itemService.findOne(itemNo);

    Member loginUser = (Member) session.getAttribute("loginUser");

    if(loginUser.getMemberCategory() == A) {
    // 사진 상태를 'N'으로 변경
    if (item != null) {
      if (item.getItemAttaches() != null && !item.getItemAttaches().isEmpty()) {
        for (Attach attach : item.getItemAttaches()) {
          attachService.update(attach.getFileNo());
        }
      }

      itemService.deleteItem(itemNo);
      return ResponseEntity.ok("Item deleted successfully");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
    }
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("관리자 권한이 필요합니다");
    }
  }

  //사진삭제
  @DeleteMapping("/api/item/deletefile/{fileNo}")
  public ResponseEntity<String> fileItemDelete(HttpSession session, @PathVariable Long fileNo) {
    Member loginUser = (Member) session.getAttribute("loginUser");

    if(loginUser.getMemberCategory() == A) {
      attachService.update(fileNo);
      return ResponseEntity.ok("File deleted successfully");
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("관리자 권한이 필요합니다");
    }
  }

  //디테일(수정)
  @GetMapping("/api/item/detail/{itemNo}")
  public ResponseEntity<Item> detailItem(@PathVariable("itemNo") Long itemNo) {
    Item item = itemService.findOne(itemNo);

      if (item != null) {
        item.setItemAttaches((item.getItemAttaches()));
        return ResponseEntity.ok(item);
      } else {
        return ResponseEntity.notFound().build();
      }

  }

  //수정 처리
  @PostMapping("/api/item/update/{itemNo}")
  public ResponseEntity<String> updateItem(HttpSession session, Item item,
          @PathVariable("itemNo") Long itemNo,
          @RequestParam(name = "multipartFiles",required = false) List<MultipartFile> multipartFiles
  ) {
    Item updatedItem = itemService.findOne(itemNo);

    Member loginUser = (Member) session.getAttribute("loginUser");

    if(loginUser.getMemberCategory() == A) {

      if (multipartFiles != null && multipartFiles.size() > 0) {
        ArrayList<Attach> attaches = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
          if (multipartFile.getSize() > 0) {
            Attach attach = ncpObjectStorageService.uploadFile(new Attach(),
                    "bleuauction-bucket", "item/", multipartFile);
            updatedItem.addItemAttach(attach);
          }
        }
      }
      if (updatedItem != null) {
        itemService.update(item);
        log.info("item/update");
        return ResponseEntity.ok("Item updated successfully");
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found or update failed");
      }
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("관리자 권한이 필요합니다");
    }

  }

}
