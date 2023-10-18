package bleuauction.bleuauction_be.server.item.entity;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@DynamicInsert
@Table(name = "ba_item")
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long itemNo;

  @JsonBackReference
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="member_no")
  private Member memberNo;

  //@Column(columnDefinition = "VARCHAR(1) DEFAULT 'S'")
  @Enumerated(EnumType.STRING)
  private ItemCode itemCode; // 종류 [S:생선/횟감, F:생선/비횟감,  C:갑각류,  M:패류  E:기타]

  private String itemName;

  @Enumerated(EnumType.STRING)
  private ItemSize itemSize; // 크기 [S,M,L]

  @Enumerated(EnumType.STRING)
  private OriginStatus originStatus; // 생산지 [D:국내, I:국외]

  @Enumerated(EnumType.STRING)
  private OriginPlaceStatus originPlaceStatus; // 동해:ES, 서해:WS,남해:SS, 제주:JJ, 완도:WD, 일본:JP, 중국:CN, 러시아:RU, 노르웨이:NW

  @Enumerated(EnumType.STRING)
  private WildFarmStatus wildFarmStatus; // 자연산여부 [W,F]

  @CreationTimestamp
  private Timestamp regDatetime;

  @UpdateTimestamp
  private Timestamp mdfDatetime;

  @Enumerated(EnumType.STRING)
  private ItemStatus itemStatus; // 상태 [Y,N]

  @JsonManagedReference
  @OneToMany(mappedBy = "itemNo", cascade=CascadeType.ALL)
  private List<Attach> itemAttaches = new ArrayList<>();

  // 비지니스 로직
  // 공지사항 삭제
  public void delete(){
    this.setItemStatus(ItemStatus.N);
  }

  // 이미지 추가를 위한 메서드
  public void addItemAttach(Attach attach) {
    itemAttaches.add(attach);
    attach.setItemNo(this); // 이미지와 메뉴를 연결
  }
}
