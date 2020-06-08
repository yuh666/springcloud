package org.yuhao.springcloud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult<T> {

    private int code;
    private String message;
    private T data;

    public boolean isSuccess() {
        return code == 0;
    }

    public static <T> ResponseResult success(T data) {
        return new ResponseResult(0, "ok", data);
    }

    public static ResponseResult success() {
        return success(null);
    }

    public static ResponseResult fail(int code, String message) {
        return new ResponseResult(code, message, null);
    }


}
