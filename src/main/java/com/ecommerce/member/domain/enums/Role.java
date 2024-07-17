package com.ecommerce.member.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    GUEST("미인증 사용자"), USER("일반 사용자"), ADMIN("관리자");

    private String value;

}
