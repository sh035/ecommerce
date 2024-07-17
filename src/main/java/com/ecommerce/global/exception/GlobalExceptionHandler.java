package com.ecommerce.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ErrorResponse handlerCustomException(CustomException e) {
        log.error("{} is occured.", e.getErrorMessage());

        return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
    }
}
