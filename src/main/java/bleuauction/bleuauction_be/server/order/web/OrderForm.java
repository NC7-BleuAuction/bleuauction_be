package bleuauction.bleuauction_be.server.order.web;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.menu.entity.Menu;
import bleuauction.bleuauction_be.server.store.entity.Store;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderForm {

  //private Coupon couponNo;
  private String orderRequest;
  @NotEmpty(message = "모든 내용은 필수입니다.")
  private Store storeNo;
  private Member memberNo;
  private Menu menuNo;
  private Long orderPrice;
  private Long orderCount;
  private String recipientPhone;
  private String recipientName;
  private String recipientZipcode;
  private String recipientAddr;
  private Long recipientDetailAddr;

}
