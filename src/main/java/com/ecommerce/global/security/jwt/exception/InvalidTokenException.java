package com.ecommerce.global.security.jwt.exception;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super("잘못된 형식의 토큰입니다.");
    }
}
