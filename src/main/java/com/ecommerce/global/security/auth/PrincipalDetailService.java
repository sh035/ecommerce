package com.ecommerce.global.security.auth;

import com.ecommerce.member.domain.entity.Member;
import com.ecommerce.member.repository.MemberRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalDetailService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String memberId) {

    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));

    return new PrincipalDetails(member.getMemberId(), member.getEmail(),
        member.getRole().toString());
  }
}
