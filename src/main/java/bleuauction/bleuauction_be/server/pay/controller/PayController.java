package bleuauction.bleuauction_be.server.pay.controller;

import bleuauction.bleuauction_be.server.member.entity.Member;
import bleuauction.bleuauction_be.server.order.entity.Order;
import bleuauction.bleuauction_be.server.order.repository.OrderRepository;
import bleuauction.bleuauction_be.server.order.service.OrderService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/pay")
public class PayController {

    private final IamportClient iamportClient;
    private final String restApiKey;
    private final String restApiSecret;


    public PayController(
            @Value("${iamport.key}") String restApiKey,
            @Value("${iamport.secret}") String restApiSecret) {
        this.restApiKey = restApiKey;
        this.restApiSecret = restApiSecret;
        this.iamportClient = new IamportClient(this.restApiKey, this.restApiSecret);
    }

    @PostMapping("/verifyIamport/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable("imp_uid") String imp_uid) throws IamportResponseException, IOException {
        return iamportClient.paymentByImpUid(imp_uid);
    }

    @GetMapping("{orderNo}")
    public ResponseEntity<Object> detail(@PathVariable Long orderNo, OrderRepository orderRepository) throws Exception {
        Optional<Order> orderOptional = Optional.ofNullable(orderRepository.findOne(orderNo));

        if (orderOptional.isPresent()) {

            Order order = orderOptional.get();
            return ResponseEntity.ok().body(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
