package com.ecommerce.member.service;

import com.ecommerce.config.RedisUtil;
import com.ecommerce.global.exception.CustomException;
import com.ecommerce.global.exception.ErrorCode;
import com.ecommerce.global.security.jwt.JwtToken;
import com.ecommerce.global.security.auth.PrincipalDetailService;
import com.ecommerce.global.security.jwt.TokenProvider;
import com.ecommerce.mail.service.MailService;
import com.ecommerce.member.domain.dto.MemberOAuthUpdateDto;
import com.ecommerce.member.domain.dto.MemberConfirmEmailDto;
import com.ecommerce.member.domain.dto.MemberDto;
import com.ecommerce.member.domain.dto.MemberSignInRequestDto;
import com.ecommerce.member.domain.dto.MemberSignInResponseDto;
import com.ecommerce.member.domain.dto.MemberSignupDto;
import com.ecommerce.member.domain.dto.MemberWithdrawalDto;
import com.ecommerce.member.domain.entity.Member;
import com.ecommerce.member.domain.enums.Role;
import com.ecommerce.member.domain.dto.MemberUpdateDto;
import com.ecommerce.member.repository.MemberRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final PrincipalDetailService principalDetailService;
  private final TokenProvider tokenProvider;
  private final MailService mailService;
  private final RedisUtil redisUtil;

  @Transactional
  public MemberSignupDto signUp(MemberSignupDto dto) {

    checkDuplicatedEmail(dto.getEmail());
    checkDuplicatedNickname(dto.getNickname());

    mailService.sendMessage(dto.getEmail());

    return MemberSignupDto.fromEntity(
        memberRepository.save(Member.builder()
            .email(dto.getEmail())
            .password(passwordEncoder.encode(dto.getPassword()))
            .nickname(dto.getNickname())
            .phone(dto.getPhone())
            .point(0)
            .authProvider(null)
            .role(Role.GUEST)
            .deletedAt(null)
            .build())
    );
  }

  @Transactional
  public boolean confirmEmail(MemberConfirmEmailDto dto) {
    Member member = memberRepository.findByEmail(dto.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    if (!mailService.checkAuthNum(dto.getEmail(), dto.getAuthNum())) {
      throw new CustomException(ErrorCode.NOT_MATCH_AUTH);
    }
    if (member.getRole() == Role.USER) {
      throw new CustomException(ErrorCode.ALREADY_VERIFY);
    }
    if (redisUtil.getData(dto.getAuthNum()) == null) {
      throw new CustomException(ErrorCode.EXPIRE_CODE);
    }
    member.setRole(Role.USER);
    return true;
  }

  @Transactional
  public MemberSignInResponseDto signIn(MemberSignInRequestDto dto) {
    UserDetails userDetails = principalDetailService.loadUserByUsername(dto.getEmail());

    if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) {
      throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);
    }

    Member member = memberRepository.findByEmail(dto.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    if (member.getDeletedAt() != null) {
      throw new CustomException(ErrorCode.DELETED_MEMBER);
    }

    if (member.getRole() == Role.GUEST) {
      throw new CustomException(ErrorCode.NOT_VERIFY_EMAIL);
    }

    JwtToken token = tokenProvider.generateToken(userDetails.getUsername(),
        userDetails.getAuthorities());

    return MemberSignInResponseDto.builder()
        .email(member.getEmail())
        .name(member.getNickname())
        .jwtToken(token)
        .build();
  }

  @Transactional
  public MemberDto updatePhone(Member member, MemberOAuthUpdateDto dto) {
    member.setPhone(dto.getPhone());

    return MemberDto.fromEntity(memberRepository.save(member));
  }

  @Transactional
  public MemberDto update(Member member, MemberUpdateDto dto) {
    member.update(dto, passwordEncoder);

    return MemberDto.fromEntity(memberRepository.save(member));
  }

  @Transactional
  public boolean withdrawal(Member member, MemberWithdrawalDto dto) {
    if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
      throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);
    }

    member.setDeletedAt(LocalDateTime.now());
    memberRepository.save(member);
    return true;
  }

  // 이메일 중복 확인
  private void checkDuplicatedEmail(String email) {
    if (memberRepository.existsByEmail(email)) {
      throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
    }
  }

  // 닉네임 중복 확인
  private void checkDuplicatedNickname(String nickname) {
    if (memberRepository.existsByNickname(nickname)) {
      throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
    }
  }
}
