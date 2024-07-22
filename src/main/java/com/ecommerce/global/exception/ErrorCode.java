package com.ecommerce.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Member
    DUPLICATED_EMAIL("이미 존재하는 이메일입니다."),
    DUPLICATED_NICKNAME("이미 존재하는 닉네임입니다."),
    NOT_MATCH_PASSWORD("비밀번호가 일치하지 않습니다."),
    DELETED_MEMBER("탈퇴한 회원입니다."),
    NOT_FOUND_MEMBER("존재하지 않는 회원입니다."),

    // Token
    EXPIRED_TOKEN("만료된 토큰입니다."),
    NOT_FOUND_TOKEN("존재하지 않는 토큰입니다."),
    NO_PERMISSION_IN_TOKEN("권한 정보가 없는 토큰입니다."),
    INVALID_TOKEN("잘못된 형식의 토큰입니다."),
    INVALID_SIGNATURE("유효하지 않은 서명입니다."),

    // Mail
    EMAIL_DELIVERY_FAILED("이메일 인증코드 전송 실패"),
    NOT_MATCH_AUTH("인증코드가 일치하지 않습니다."),
    ALREADY_VERIFY("이미 인증이 완료되었습니다."),
    EXPIRE_CODE("인증 시간이 만료되었습니다."),
    NOT_VERIFY_EMAIL("이메일 인증을 완료해주세요."),

    // Category
    NOT_FOUND_CATEGORY("존재하지 않는 카테고리 입니다."),
    NOT_FOUND_PARENT_CATEGORY("부모 카테고리가 존재하지 않습니다.")
    ;

    private final String description;
}
