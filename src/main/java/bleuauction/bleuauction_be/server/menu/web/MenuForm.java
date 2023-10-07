package bleuauction.bleuauction_be.server.menu.web;

import bleuauction.bleuauction_be.server.menu.entity.MenuSize;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuForm {

  @NotEmpty(message = "내용은 필수입니다.")
  @Column(name="menu_name")
  public String menuName;

  @Column(name="menu_size")
  public MenuSize menuSize;

  @Column(name="menu_price")
  public String menuPrice;

  @Column(name="menu_content")
  public String menuContent;
}
