package bleuauction.bleuauction_be.server.item.entity;

import org.hibernate.annotations.DynamicInsert;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@DynamicInsert
@Table(name = "ba_item")
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long itemNo;

  @Enumerated(EnumType.STRING)
  private ItemCode itemCode; // 종류 [S:생선/횟감, F:생선/비횟감,  C:갑각류,  M:패류  E:기타]

//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name ="origin_country_no",  referencedColumnName = "originCountryNo")
//  private Long originCountryNo;

  @Enumerated(EnumType.STRING)
  private OriginStatus originStatus; // 생산지 [I:국내, O:국외]

  @Enumerated(EnumType.STRING)
  private OriginPlaceStatus originPlaceStatus; // 동해:ES, 서해:WS,남해:SS, 제주:JJ, 완도:WD, 일본:JP, 중국:CN, 러시아:RU, 노르웨이:NW

  private String itemName;

  @Enumerated(EnumType.STRING)
  private ItemSize itemSize; // 크기 [S,M,L]

  @Enumerated(EnumType.STRING)
  private WildFarmStatus wildFarmStatus; // 자연산여부 [W,F]

  @Enumerated(EnumType.STRING)
  private ItemStatus itemStatus; // 상태 [Y,N]

  // 비지니스 로직
  // 공지사항 삭제
  public void delete(){
    this.setItemStatus(ItemStatus.N);
  }
}
