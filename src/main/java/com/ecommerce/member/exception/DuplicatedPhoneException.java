package com.ecommerce.member.exception;

public class DuplicatedPhoneException extends RuntimeException {
    public DuplicatedPhoneException() {
        super("기존 전화번호와 동일합니다.");
    }

}
