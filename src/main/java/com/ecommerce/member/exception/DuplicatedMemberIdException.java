package com.ecommerce.member.exception;

public class DuplicatedMemberIdException extends RuntimeException{
    public DuplicatedMemberIdException() {
        super("이미 존재하는 아이디입니다.");
    }

}
