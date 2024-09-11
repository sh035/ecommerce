package com.ecommerce.member.domain.dto;

import com.ecommerce.member.domain.entity.Member;
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
public class MemberSignupDto {

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    @Pattern(regexp = "^[a-z0-9]{6,12}$", message = "아이디는 영문 소문자와 숫자 6~12자리여야 합니다.")
    private String memberId;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
        message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z].*)(?=.*[0-9!@#$%^&*()\\-_=+{};:,<.>].*).{4,16}$",
        message = "비밀번호는 4자에서 16자 사이로 영문자, 숫자, 특수문자 중 2가지 이상을 조합해야 합니다.")
    private String password;

    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "^010\\d{7,8}$", message = "전화번호는 010으로 시작하는 11자리 숫자여야 합니다.")
    private String phone;

    @NotBlank(message = "인증 번호를 입력해주세요.")
    @Pattern(regexp = "^[0-9]{4}$", message = "인증 번호는 4자리 숫자여야 합니다.")
    private String authNum;

    public static MemberSignupDto from(Member member) {
        return MemberSignupDto.builder()
            .email(member.getEmail())
            .password(member.getPassword())
            .memberId(member.getMemberId())
            .phone(member.getPhone())
            .build();
    }
}
