package com.ecommerce.member.exception;

public class PasswordMatchFailException extends RuntimeException{

    public PasswordMatchFailException() {
        super("비밀번호가 일치하지 않습니다.");
    }

}
