package bleuauction.bleuauction_be.server.pay.controller;

import bleuauction.bleuauction_be.server.common.utils.JwtUtils;
import bleuauction.bleuauction_be.server.order.service.OrderService;
import bleuauction.bleuauction_be.server.pay.dto.PayInsertRequest;
import bleuauction.bleuauction_be.server.pay.entity.Pay;
import bleuauction.bleuauction_be.server.pay.service.PayService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pay")
public class PayController {
    private final IamportClient iamportClient;
    private final OrderService orderService;
    private final JwtUtils jwtUtils;
    private final PayService payService;

    /**
     * 요청한 PayNo의 결제 정보 조회 <br />
     * [TODO] : 인증, 인가가 필요 없는 기능인지?
     * @param payNo
     * @return
     * @throws Exception
     */
    @GetMapping("/{payNo}")
    public ResponseEntity<Object> detail(@PathVariable Long payNo) throws Exception {
        return ResponseEntity.ok(payService.getPay(payNo));
    }

    /**
     * 아임포트로 결제를 요청후 성공시 받게되는 결제정보 데이터를 생성하는 것으로 유추됨
     *
     * @param authorizationHeader
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<Pay> createPayment(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody PayInsertRequest request
    ) {
        jwtUtils.verifyToken(authorizationHeader);
        return ResponseEntity.ok(
                payService.createPayment(
                        request, orderService.findOrderById(request.getOrderNo())
                )
        );
    }

    /**
     * 아임포트에 iamportUid를 요청시 결제여부 검증을 진행하는 기능<br />
     * https://github.com/iamport/iamport-rest-client-java/blob/master/README.md
     * @param imp_uid
     * @return
     * @throws IamportResponseException
     * @throws IOException
     */
    @PostMapping("/verifyIamport/{iamportUid}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable("iamportUid") String imp_uid) throws
            IamportResponseException, IOException {
        return iamportClient.paymentByImpUid(imp_uid);
    }
}
