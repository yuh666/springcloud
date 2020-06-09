package org.yuhao.springcloud.payment.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yuhao.springcloud.common.dto.ResponseResult;
import org.yuhao.springcloud.common.dto.payment.PaymentReqDto;
import org.yuhao.springcloud.common.dto.payment.PaymentRespDto;
import org.yuhao.springcloud.payment.service.IPaymentService;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private IPaymentService paymentSrervice;

    @PostMapping("/add")
    public Object create(@RequestBody PaymentReqDto dto) {
        long id = paymentSrervice.create(dto);
        return ResponseResult.success(id);
    }

    @GetMapping("/get/{id}")
    public Object get(@PathVariable("id") @Min(1) Long id) {
        PaymentRespDto paymentRespDto = paymentSrervice.get(id);
        return ResponseResult.success(paymentRespDto);
    }

}
