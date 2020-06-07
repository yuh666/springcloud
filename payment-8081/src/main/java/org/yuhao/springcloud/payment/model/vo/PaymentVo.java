package org.yuhao.springcloud.payment.model.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zy-user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVo {

    private Long id;
    private String serial;
}
