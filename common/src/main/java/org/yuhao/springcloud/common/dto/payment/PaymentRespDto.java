package org.yuhao.springcloud.common.dto.payment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yuhao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRespDto {

    private Long id;
    private String serial;
}
