package com.ecommerce.member.domain.entity;

import com.ecommerce.member.domain.enums.Role;
import com.ecommerce.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@SQLRestriction("deleted_at IS NULL")
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberId;

    private String email;

    private String password;

    private String phone;

    private int point;

    private String authProvider;
    private String providerId;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime deletedAt;

    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .memberId(memberId)
                .email(email)
                .password(encodedPassword)
                .phone(phone)
                .point(point)
                .authProvider(authProvider)
                .role(Role.USER)
                .deletedAt(null)
                .build();
    }

    public void updatePhone(String phone) {
        this.phone = phone;
    }

    public void withdrawal(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void update(String password, String phone) {
        this.password = password;
        this.phone = phone;
    }

    public void charge(int point) {
        this.point -= point;
    }
}
