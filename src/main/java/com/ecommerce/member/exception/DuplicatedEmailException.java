package com.ecommerce.member.exception;

public class DuplicatedEmailException extends RuntimeException{
    public DuplicatedEmailException() {
        super("이미 존재하는 이메일입니다.");
    }
}
