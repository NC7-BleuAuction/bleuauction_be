package bleuauction.bleuauction_be.server.attach.entity;

import bleuauction.bleuauction_be.server.menu.entity.Menu;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorValue("MENU")
public class MenuAttach extends Attach {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_no")
    private Menu menu;

    public MenuAttach(AttachVO attachVO) {
        super(attachVO);
    }

    public MenuAttach(AttachVO attachVO, Menu menu) {
        this(attachVO);
        this.menu = menu;
    }

    // 파일추가 및 메뉴 추가
    public void setMenu(Menu menu) {
        this.menu = menu;
        menu.getAttachs().add(this);
    }
}
