package com.ecommerce.member.domain.entity;

import com.ecommerce.member.domain.enums.Role;
import com.ecommerce.global.entity.BaseTime;
import com.ecommerce.member.domain.dto.MemberUpdateDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;
    private String nickname;

    private String phone;

    private int point;

    private String authProvider;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime deletedAt;

    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .email(email)
                .password(encodedPassword)
                .nickname(nickname)
                .phone(phone)
                .point(point)
                .authProvider(authProvider)
                .role(Role.USER)
                .deletedAt(null)
                .build();
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void update(MemberUpdateDto dto, PasswordEncoder passwordEncoder) {
        this.nickname = dto.getNickname();
        this.password = passwordEncoder.encode(dto.getPassword());
        this.phone = dto.getPhone();
    }
}
