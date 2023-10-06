package bleuauction.bleuauction_be.server.menu.entity;

import bleuauction.bleuauction_be.server.notice.entity.NoticeStatus;
import bleuauction.bleuauction_be.server.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

  @Entity
  @Getter
  @Setter
  @Table(name = "ba_menu")
  public class Menu {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="menu_no")
  private Long menuNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="store_no")
  private Store store;

  @Column(name="menu_name")
  private String menuName;

  @Enumerated(EnumType.STRING)
  @Column(name="menu_size")
  private MenuSize menuSize;

  @Column(name="menu_price")
  private String menuPrice;

  @Column(name="menu_content")
  private String menuContent;

  @CreationTimestamp
  @Column(name="reg_datetime")
  private LocalDateTime regDatetime;

  @UpdateTimestamp
  @Column(name="mdf_datetime")
  private LocalDateTime mdfDatetime;

  @Enumerated(EnumType.STRING)
  @Column(name="menu_status", columnDefinition = "VARCHAR(1) DEFAULT 'Y'")
  private MenuStatus menuStatus; // 상태 [Y,N]

  // 비지니스 로직
  // 공지사항 삭제
  public void delete(){
    this.setMenuStatus(MenuStatus.N);
  }

}
