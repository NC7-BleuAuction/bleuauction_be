package bleuauction.bleuauction_be.server.menu.entity;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.notice.entity.NoticeStatus;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.store.entity.Store;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicInsert
@Table(name = "ba_menu")
public class Menu {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long menuNo;

  @JsonBackReference
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="store_no")
  private Store storeNo;

  private String menuName;

  @Enumerated(EnumType.STRING)
  private MenuSize menuSize;

  private int menuPrice;

  private String menuContent;

  @CreationTimestamp
  private Timestamp regDatetime;

  @UpdateTimestamp
  private Timestamp mdfDatetime;

  @Enumerated(EnumType.STRING)
  @Column(name="menu_status", columnDefinition = "VARCHAR(1) DEFAULT 'Y'")
  private MenuStatus menuStatus; // 상태 [Y,N]

  @JsonManagedReference
  @OneToMany(mappedBy = "menuNo")
  private List<Order> orders = new ArrayList<>();

  @JsonManagedReference
  @OneToMany(mappedBy = "menuNo", cascade=CascadeType.ALL)
  private List<Attach> menuAttaches = new ArrayList<>();

  // 비지니스 로직
  // 공지사항 삭제
  public void delete(){
    this.setMenuStatus(MenuStatus.N);
  }

  // 이미지 추가를 위한 메서드
  public void addAttach(Attach attach) {
    menuAttaches.add(attach);
    attach.setMenuNo(this); // 이미지와 메뉴를 연결
  }

}
