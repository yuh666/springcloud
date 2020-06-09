package org.yuhao.springcloud.payment.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.yuhao.springcloud.common.dto.ResponseResult;
import org.yuhao.springcloud.common.exception.CommonException;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class APIExceptionHandler {


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseResult<?> handleFormInvalidError(ConstraintViolationException e) {
        return ResponseResult.fail(-1, e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public ResponseResult<?> handleFormInvalidError(BindException e) {
        return ResponseResult.fail(-1, e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseResult<?> handlerMissingParameterError(
            MissingServletRequestParameterException e) {
        return ResponseResult.fail(-1, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<?> handlerMethodArgumentNotValidError(MethodArgumentNotValidException e) {
        return ResponseResult.fail(-1, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseResult<?> handle405(HttpRequestMethodNotSupportedException e) {
        return ResponseResult.fail(405, e.getMessage());
    }

    @ExceptionHandler(CommonException.class)
    public ResponseResult<?> handleError(CommonException e) {
        return ResponseResult.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseResult<?> handleUnknownError(Throwable e) {
        log.error("unknown error happened", e);
        return ResponseResult.fail(-999, e.getMessage());
    }
}