package com.ecommerce.mail.exception;

public class AuthDeliveryFailException extends RuntimeException {
    public AuthDeliveryFailException() {
        super("이메일 인증코드 전송을 실패하였습니다.");
    }
}
