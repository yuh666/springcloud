package org.yuhao.springcloud.payment.service;

import org.yuhao.springcloud.payment.model.dto.PaymentDto;
import org.yuhao.springcloud.payment.model.vo.PaymentVo;

public interface IPaymentSrervice {

    long create(PaymentDto dto);

    PaymentVo get(Long id);

}
