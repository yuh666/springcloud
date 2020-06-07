package org.yuhao.springcloud.payment.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author zy-user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    @NotBlank(message = "serial cannot be null")
    private String serial;
}
