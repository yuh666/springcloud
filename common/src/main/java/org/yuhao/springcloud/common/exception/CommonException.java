package org.yuhao.springcloud.common.exception;

/**
 * TODO:DOCUMENT ME!
 *
 * @author yuhao
 * @date 2020/6/8 2:37 下午
 */
public class CommonException extends RuntimeException {

    private int code;

    public CommonException(String message, int code) {
        super(message);
        this.code = code;
    }

    public CommonException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
