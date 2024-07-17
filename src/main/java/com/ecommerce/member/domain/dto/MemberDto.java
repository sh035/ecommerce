package com.ecommerce.member.domain.dto;

import com.ecommerce.member.domain.entity.Member;
import com.ecommerce.member.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {

    private String email;
    private String password;
    private String nickname;
    private String phone;
    private int point;
    private String authProvider;
    private Role role;
    private LocalDateTime deletedAt;

    public static MemberDto fromEntity(Member member) {
        return MemberDto.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .phone(member.getPhone())
                .point(member.getPoint())
                .authProvider(member.getAuthProvider())
                .role(member.getRole())
                .deletedAt(member.getDeletedAt())
                .build();
    }

    public Member toEntity() {
        return Member.builder()
            .email(email)
            .password(password)
            .nickname(nickname)
            .phone(phone)
            .point(point)
            .authProvider(authProvider)
            .role(role)
            .deletedAt(deletedAt)
            .build();
    }
}
