package bleuauction.bleuauction_be.server.menu.entity;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import bleuauction.bleuauction_be.server.attach.entity.Attach;
import bleuauction.bleuauction_be.server.attach.entity.MenuAttach;
import bleuauction.bleuauction_be.server.common.entity.AbstractTimeStamp;
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
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ba_menu")
@NoArgsConstructor(access = PROTECTED)
public class Menu extends AbstractTimeStamp {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JoinColumn(name = "menu_no")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_no")
    private Store store;

    private String name;

    @Enumerated(STRING)
    private MenuSize size;

    private int price;

    @Lob private String content;

    @Enumerated(STRING)
    private MenuStatus status; // 상태 [Y,N]

    @OneToMany(mappedBy = "menu", cascade = ALL)
    private List<MenuAttach> attachs = new ArrayList<>();

    @OneToMany(mappedBy = "menu", cascade = ALL)
    private List<OrderMenu> orderMenus = new ArrayList<>();

    @Builder
    public Menu(
            Store store, String name, MenuSize size, int price, String content, MenuStatus status) {
        this.store = store;
        this.name = name;
        this.size = size;
        this.price = price;
        this.content = content;
        this.status = status;
    }

    // 비지니스 로직
    public void delete() {
        this.deleteAttaches();
        this.setStatus(MenuStatus.N);
    }

    public void addStore(Store store) {
        this.store = store;
        store.getMenus().add(this);
    }

    public void deleteAttaches() {
        this.attachs.forEach(Attach::deleteAttach);
    }
}
