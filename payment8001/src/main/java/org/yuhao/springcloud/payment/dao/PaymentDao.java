package org.yuhao.springcloud.payment.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.yuhao.springcloud.payment.model.po.PaymentPo;

@Mapper
public interface PaymentDao {

    int create(PaymentPo paymentPo);

    PaymentPo get(@Param("id") Long id);
}
