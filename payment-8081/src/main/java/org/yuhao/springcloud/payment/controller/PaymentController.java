package org.yuhao.springcloud.payment.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yuhao.springcloud.payment.model.dto.PaymentDto;
import org.yuhao.springcloud.payment.model.vo.PaymentVo;
import org.yuhao.springcloud.payment.model.vo.ResponseResult;
import org.yuhao.springcloud.payment.service.IPaymentSrervice;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private IPaymentSrervice paymentSrervice;

    @PostMapping("/add")
    public Object create(PaymentDto dto) {
        long id = paymentSrervice.create(dto);
        return ResponseResult.success(id);
    }

    @GetMapping("/get/{id}")
    public Object get(@PathVariable("id") @Min(1) Long id) {
        PaymentVo paymentVo = paymentSrervice.get(id);
        return ResponseResult.success(paymentVo);
    }

}
