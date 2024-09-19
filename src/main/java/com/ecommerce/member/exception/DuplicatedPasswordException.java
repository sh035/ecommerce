package com.ecommerce.member.exception;

public class DuplicatedPasswordException extends RuntimeException{

    public DuplicatedPasswordException() {
        super("기존 패스워드와 동일합니다.");
    }

}
