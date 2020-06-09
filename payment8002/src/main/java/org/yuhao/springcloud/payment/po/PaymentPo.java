package org.yuhao.springcloud.payment.po;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zy-user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPo {

    private Long id;
    private String serial;
}
