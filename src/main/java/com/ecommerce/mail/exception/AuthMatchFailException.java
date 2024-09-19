package com.ecommerce.mail.exception;

public class AuthMatchFailException extends RuntimeException {

    public AuthMatchFailException() {
        super("인증코드가 일치하지 않습니다.");
    }
}
