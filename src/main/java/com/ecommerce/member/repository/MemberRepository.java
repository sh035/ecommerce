package com.ecommerce.member.repository;

import com.ecommerce.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberId(String memberId);
    Optional<Member> findByEmail(String email);
    boolean existsByMemberId(String memberId);
    boolean existsByEmail(String email);
}
