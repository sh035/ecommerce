package com.ecommerce.member.service;

import com.ecommerce.global.security.jwt.JwtRedisService;
import com.ecommerce.global.security.jwt.JwtTokenDto;
import com.ecommerce.global.security.jwt.TokenProvider;
import com.ecommerce.mail.exception.AuthMatchFailException;
import com.ecommerce.mail.service.MailRedisService;
import com.ecommerce.member.domain.dto.MemberOAuthUpdateDto;
import com.ecommerce.member.domain.dto.MemberSignInRequestDto;
import com.ecommerce.member.domain.dto.MemberSignInResponseDto;
import com.ecommerce.member.domain.dto.MemberSignupDto;
import com.ecommerce.member.domain.dto.MemberWithdrawalDto;
import com.ecommerce.member.domain.entity.Member;
import com.ecommerce.member.domain.enums.Role;
import com.ecommerce.member.domain.dto.MemberUpdateDto;
import com.ecommerce.member.exception.DuplicatedEmailException;
import com.ecommerce.member.exception.DuplicatedMemberIdException;
import com.ecommerce.member.exception.DuplicatedPasswordException;
import com.ecommerce.member.exception.DuplicatedPhoneException;
import com.ecommerce.member.exception.PasswordMatchFailException;
import com.ecommerce.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final JwtRedisService jwtRedisService;
    private final MailRedisService mailRedisService;

    @Transactional
    public void signUp(MemberSignupDto dto) {

        if (memberRepository.existsByMemberId(dto.getMemberId())) {
            throw new DuplicatedMemberIdException();
        }
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicatedEmailException();
        }
        if (!dto.getEmail().equals(mailRedisService.getData(dto.getAuthNum()))) {
            throw new AuthMatchFailException();
        }

        memberRepository.save(Member.builder()
            .memberId(dto.getMemberId())
            .email(dto.getEmail())
            .password(passwordEncoder.encode(dto.getPassword()))
            .phone(dto.getPhone())
            .role(Role.USER)
            .build());

        mailRedisService.deleteData(dto.getAuthNum());
    }

    @Transactional
    public MemberSignInResponseDto signIn(MemberSignInRequestDto dto) {

        Member member = memberRepository.findByMemberId(dto.getMemberId())
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new PasswordMatchFailException();
        }

        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(member.getRole().toString()));

        JwtTokenDto token = tokenProvider.generateToken(member.getMemberId(),
            authorities);

        long refreshTokenExpiration = tokenProvider.getExpiration(token.getRefreshToken());

        jwtRedisService.save(token, refreshTokenExpiration);

        return MemberSignInResponseDto.builder()
            .memberId(member.getMemberId())
            .accessToken(token.getAccessToken())
            .build();
    }

    @Transactional
    public void updatePhone(String memberId, MemberOAuthUpdateDto dto) {
        Member member = memberRepository.findByMemberId(memberId)
            .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));
        member.updatePhone(dto.getPhone());

        memberRepository.save(member);
    }

    @Transactional
    public void update(String memberId, MemberUpdateDto dto) {
        Member member = memberRepository.findByMemberId(memberId)
            .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));

        if (passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new DuplicatedPasswordException();
        }

        if (dto.getPhone().equals(member.getPhone())) {
            throw new DuplicatedPhoneException();
        }

        member.update(passwordEncoder.encode(dto.getPassword()), dto.getPhone());
        memberRepository.save(member);
    }

    public void logout(String memberId, HttpServletRequest request) {
        String accessToken = tokenProvider.resolveToken(request);
        long accessTokenExpiration = tokenProvider.getExpiration(accessToken);

        jwtRedisService.deleteRefreshTokenByAccessToken(accessToken);
        jwtRedisService.setBlackList(accessToken, memberId, accessTokenExpiration);

        SecurityContextHolder.clearContext();
    }

    public void withdrawal(String memberId, MemberWithdrawalDto dto) {
        Member member = memberRepository.findByMemberId(memberId)
            .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));

        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new PasswordMatchFailException();
        }

        member.withdrawal(LocalDateTime.now());
        memberRepository.save(member);
    }
}
