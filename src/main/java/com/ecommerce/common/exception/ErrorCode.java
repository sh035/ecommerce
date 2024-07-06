package com.ecommerce.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Member
    DUPLICATED_EMAIL("이미 존재하는 이메일입니다."),
    DUPLICATED_NICKNAME("이미 존재하는 닉네임입니다.")
    ;

    private final String description;
}
