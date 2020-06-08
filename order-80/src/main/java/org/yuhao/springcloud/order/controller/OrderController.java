package org.yuhao.springcloud.order.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.yuhao.springcloud.common.dto.ResponseResult;
import org.yuhao.springcloud.common.dto.payment.PaymentReqDto;
import org.yuhao.springcloud.common.dto.payment.PaymentRespDto;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/order")
public class OrderController {

    private static final String paymentHost = "http://localhost:8001/payment";

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/add")
    public Object create(PaymentReqDto dto) {
        ResponseResult<String> responseResult = restTemplate.postForObject(
                paymentHost + "/add", dto, ResponseResult.class);
        if(!responseResult.isSuccess()){
            return ResponseResult.fail(1,"fail");
        }
        return ResponseResult.success(responseResult.getData());
    }

    @GetMapping("/get/{id}")
    public Object get(@PathVariable("id") @Min(1) Long id) {
        ResponseResult<PaymentRespDto> responseResult = restTemplate.getForObject(
                paymentHost + "/get/" + id,
                ResponseResult.class);
        if(!responseResult.isSuccess()){
            return ResponseResult.fail(1,"fail");
        }
        return ResponseResult.success(responseResult.getData());
    }

}
