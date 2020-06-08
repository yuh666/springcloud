package org.yuhao.springcloud.payment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yuhao.springcloud.common.dto.payment.PaymentReqDto;
import org.yuhao.springcloud.common.dto.payment.PaymentRespDto;
import org.yuhao.springcloud.payment.dao.PaymentDao;
import org.yuhao.springcloud.payment.model.po.PaymentPo;
import org.yuhao.springcloud.payment.service.IPaymentService;

@Service
public class PaymentServiceImpl implements IPaymentService {

    @Autowired
    private PaymentDao paymentDao;


    @Override
    public long create(PaymentReqDto dto) {
        PaymentPo paymentPo = new PaymentPo();
        paymentPo.setSerial(dto.getSerial());
        paymentDao.create(paymentPo);
        return paymentPo.getId();
    }

    @Override
    public PaymentRespDto get(Long id) {
        PaymentPo paymentPo = paymentDao.get(id);
        if(paymentPo == null){
            return null;
        }
        PaymentRespDto paymentRespDto = new PaymentRespDto();
        paymentRespDto.setId(paymentPo.getId());
        paymentRespDto.setSerial(paymentPo.getSerial());
        return paymentRespDto;
    }
}
