package bleuauction.bleuauction_be.server.menu.entity;

import bleuauction.bleuauction_be.server.attach.entity.MenuAttach;
import bleuauction.bleuauction_be.server.orderMenu.entity.OrderMenu;
import bleuauction.bleuauction_be.server.store.entity.Store;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "ba_menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JoinColumn(name = "menu_no")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_no")
    private Store store;

    private String menuName;

    @Enumerated(STRING)
    private MenuSize menuSize;

    private int menuPrice;

    @Lob
    private String menuContent;

    @CreationTimestamp
    private Timestamp regDatetime;

    @UpdateTimestamp
    private Timestamp mdfDatetime;

    @Enumerated(STRING)
    private MenuStatus menuStatus; // 상태 [Y,N]

    @OneToMany(mappedBy = "menu", cascade = ALL)
    private List<MenuAttach> attachs = new ArrayList<>();

    @OneToMany(mappedBy = "menu", cascade = ALL)
    private List<OrderMenu> orderMenus = new ArrayList<>();

    // 비지니스 로직
    // 공지사항 삭제
    public void delete() {
        this.setMenuStatus(MenuStatus.N);
    }

    public void addStore(Store store) {
        this.store = store;
        store.getMenus().add(this);
    }
}
