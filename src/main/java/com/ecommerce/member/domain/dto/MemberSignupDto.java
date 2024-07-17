package com.ecommerce.member.domain.dto;

import com.ecommerce.member.domain.entity.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSignupDto {

  @NotBlank(message = "이메일을 입력하세요.")
  private String email;

  @NotBlank(message = "비밀번호를 입력하세요.")
  private String password;

  @NotBlank(message = "닉네임을 입력하세요.")
  private String nickname;

  @NotBlank(message = "전화번호를 입력하세요.")
  private String phone;

  public static MemberSignupDto fromEntity(Member member) {
    return MemberSignupDto.builder()
        .email(member.getEmail())
        .password(member.getPassword())
        .nickname(member.getNickname())
        .phone(member.getPhone())
        .build();
  }
}
