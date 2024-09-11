package com.ecommerce.member.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberSignInRequestDto {

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    @Pattern(regexp = "^[a-z0-9]{6,12}$", message = "아이디는 영문 소문자와 숫자 6~12자리여야 합니다.")
    private String memberId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z].*)(?=.*[0-9!@#$%^&*()\\-_=+{};:,<.>].*).{4,16}$",
        message = "비밀번호는 4자에서 16자 사이로 영문자, 숫자, 특수문자 중 2가지 이상을 조합해야 합니다.")
    private String password;
}
