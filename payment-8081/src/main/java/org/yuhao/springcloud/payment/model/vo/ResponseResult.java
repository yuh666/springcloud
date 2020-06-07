package org.yuhao.springcloud.payment.model.vo;

import lombok.Getter;

@Getter
public class ResponseResult<T> {

    private int code;
    private String message;
    private T data;

    public ResponseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
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
