package com.ecommerce.global.security.auth;

import com.ecommerce.global.exception.CustomException;
import com.ecommerce.global.exception.ErrorCode;
import com.ecommerce.member.domain.entity.Member;
import com.ecommerce.member.repository.MemberRepository;
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
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    return new PrincipalDetails(member.getMemberId(), member.getEmail(),
        member.getRole().toString());
  }

}
