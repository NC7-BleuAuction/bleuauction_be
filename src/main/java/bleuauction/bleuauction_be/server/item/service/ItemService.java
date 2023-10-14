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
  public Item update(Item item) {
    Item updateItem = itemRepository.findOne(item.getItemNo());

    updateItem.setItemName(item.getItemName());
    updateItem.setItemCode(item.getItemCode());
    updateItem.setOriginStatus(item.getOriginStatus());
    updateItem.setOriginPlaceStatus(item.getOriginPlaceStatus());
    updateItem.setItemSize(item.getItemSize());
    updateItem.setWildFarmStatus(item.getWildFarmStatus());

    Item update = itemRepository.save(updateItem);
    return updateItem;
  }

}
