package com.ecommerce.member.domain.dto;

import com.ecommerce.global.security.jwt.JwtToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSignInResponseDto {
  private String email;
  private String name;
  private JwtToken jwtToken;
}
