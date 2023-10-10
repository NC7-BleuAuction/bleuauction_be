package bleuauction.bleuauction_be.server.order.controller;


import bleuauction.bleuauction_be.server.menu.web.MenuForm;
import bleuauction.bleuauction_be.server.order.service.OrderService;
import bleuauction.bleuauction_be.server.order.web.OrderForm;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;
  private final EntityManager entityManager;

  //등록페이지
  @GetMapping("/order/new")
  public String createForm(Model model) {
    model.addAttribute("orderForm", new OrderForm());
    log.info("order/new");
    return "/order/new";
  }
}
