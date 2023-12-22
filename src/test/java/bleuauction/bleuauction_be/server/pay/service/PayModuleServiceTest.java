package bleuauction.bleuauction_be.server.pay.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import bleuauction.bleuauction_be.server.pay.entity.Pay;
import bleuauction.bleuauction_be.server.pay.entity.PayStatus;
import bleuauction.bleuauction_be.server.pay.entity.PayType;
import bleuauction.bleuauction_be.server.pay.exception.PayHistoryNotFoundException;
import bleuauction.bleuauction_be.server.pay.repository.PayRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PayModuleServiceTest {
    @Mock private PayRepository payRepository;

    @InjectMocks private PayModuleService payModuleService;

    @Test
    @DisplayName("Pay정보를 조회할 때, 파라미터로 제공받은 payNo가 존재하지 않는 경우 PayHistoryNotFoundException이 발생한다.")
    void getPay_WhenPayNoIsNotExist_ThrowPayHistoryNotFoundException() {
        // given
        Long payNo = 1L;
        PayHistoryNotFoundException mockException = new PayHistoryNotFoundException(payNo);

        given(payRepository.findById(payNo)).willReturn(Optional.empty());

        // when && then
        PayHistoryNotFoundException e =
                assertThrows(
                        PayHistoryNotFoundException.class, () -> payModuleService.findById(payNo));
        assertEquals(mockException.getMessage(), e.getMessage());
    }

    @Test
    @DisplayName("Pay정보를 조회 할 떄, 파라미터로 제공받은 payNo가 존재하는 경우 Pay정보를 반환한다.")
    void getPay_WhenPayNoIsExist_ThenReturnPay() {
        // given
        Long payNo = 1L;
        Pay mockPay = Pay.builder().payType(PayType.C).price(80000).status(PayStatus.Y).build();
        mockPay.setId(payNo);

        given(payRepository.findById(payNo)).willReturn(Optional.of(mockPay));

        // when
        Pay foundPay = payModuleService.findById(payNo);

        // then
        assertEquals(mockPay, foundPay);
    }
}
