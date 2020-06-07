package org.yuhao.springcloud.payment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yuhao.springcloud.payment.dao.PaymentDao;
import org.yuhao.springcloud.payment.model.dto.PaymentDto;
import org.yuhao.springcloud.payment.model.po.PaymentPo;
import org.yuhao.springcloud.payment.model.vo.PaymentVo;
import org.yuhao.springcloud.payment.service.IPaymentSrervice;

@Service
public class PaymentServiceImpl implements IPaymentSrervice {

    @Autowired
    private PaymentDao paymentDao;


    @Override
    public long create(PaymentDto dto) {
        PaymentPo paymentPo = new PaymentPo();
        paymentPo.setSerial(dto.getSerial());
        paymentDao.create(paymentPo);
        return paymentPo.getId();
    }

    @Override
    public PaymentVo get(Long id) {
        PaymentPo paymentPo = paymentDao.get(id);
        if(paymentPo == null){
            return null;
        }
        PaymentVo paymentVo = new PaymentVo();
        paymentVo.setId(paymentPo.getId());
        paymentVo.setSerial(paymentPo.getSerial());
        return paymentVo;
    }
}
