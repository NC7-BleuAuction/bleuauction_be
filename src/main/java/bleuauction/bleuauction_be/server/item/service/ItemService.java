package bleuauction.bleuauction_be.server.item.service;

import bleuauction.bleuauction_be.server.item.entity.*;
import bleuauction.bleuauction_be.server.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository itemRepository;

  //등록
  @Transactional
  public Long enroll(Item item) {
    itemRepository.save(item);
    return item.getItemNo();
  }

  //품목 전체 조회
  @Transactional(readOnly = true)
  public List<Item> finditems() {
    return itemRepository.findAll();
  }

  //품목 1건 조회
  @Transactional(readOnly = true)
  public Item findOne(Long itemNo) {
    return itemRepository.findOne(itemNo);
  }

  //품목 삭제
  @Transactional
  public void deleteItem(Long itemNo) {
    Item item = itemRepository.findOne(itemNo);
    item.delete();
  }

  @Transactional
  public Item update(Long itemNo, ItemCode itemCode, OriginStatus originStatus, OriginPlaceStatus originPlaceStatus, String itemName, ItemSize itemSize, WildFarmStatus wildFarmStatus) {
    Item item = itemRepository.findOne(itemNo);
    item.setItemName(itemName);
    item.setItemCode(itemCode);
    item.setOriginStatus(originStatus);
    item.setOriginPlaceStatus(originPlaceStatus);
    item.setItemSize(itemSize);
    item.setWildFarmStatus(wildFarmStatus);
    return item;
  }

}
