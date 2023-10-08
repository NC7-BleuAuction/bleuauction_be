package bleuauction.bleuauction_be.server.item.web;

import bleuauction.bleuauction_be.server.item.entity.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemForm {

  @NotNull(message = "모든 내용은 필수입니다.")
  public ItemCode itemCode;
  public OriginStatus originStatus;
  public OriginPlaceStatus originPlaceStatus;
  public String itemName;
  public ItemSize itemSize;
  public WildFarmStatus wildFarmStatus;
}
