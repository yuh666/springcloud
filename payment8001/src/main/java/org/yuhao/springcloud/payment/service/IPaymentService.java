package org.yuhao.springcloud.payment.service;


import org.yuhao.springcloud.common.dto.payment.PaymentReqDto;
import org.yuhao.springcloud.common.dto.payment.PaymentRespDto;

public interface IPaymentService {

    long create(PaymentReqDto dto);

    PaymentRespDto get(Long id);

}
