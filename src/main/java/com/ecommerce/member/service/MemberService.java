package com.ecommerce.member.service;

import com.ecommerce.global.security.jwt.JwtRedisService;
import com.ecommerce.mail.service.MailRedisService;
import com.ecommerce.global.exception.CustomException;
import com.ecommerce.global.exception.ErrorCode;
import com.ecommerce.global.security.jwt.JwtTokenDto;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final MailService mailService;
    private final MailRedisService mailRedisService;
    private final JwtRedisService jwtRedisService;

    @Transactional
    public void signUp(MemberSignupDto dto) {

        checkDuplicatedEmail(dto.getEmail());
        checkDuplicatedMemberId(dto.getMemberId());

        mailService.sendMessage(dto.getEmail());

        MemberSignupDto.fromEntity(
            memberRepository.save(Member.builder()
                .memberId(dto.getMemberId())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone(dto.getPhone())
                .point(0)
                .authProvider(null)
                .role(Role.GUEST)
                .deletedAt(null)
                .build())
        );
    }

    @Transactional
    public void confirmEmail(MemberConfirmEmailDto dto) {
        Member member = memberRepository.findByEmail(dto.getEmail())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        if (!mailService.checkAuthNum(dto.getEmail(), dto.getAuthNum())) {
            throw new CustomException(ErrorCode.NOT_MATCH_AUTH);
        }
        if (member.getRole() == Role.USER) {
            throw new CustomException(ErrorCode.ALREADY_VERIFY);
        }
        if (mailRedisService.getData(dto.getAuthNum()) == null) {
            throw new CustomException(ErrorCode.EXPIRE_CODE);
        }
        member.changeRole(Role.USER);
    }

    @Transactional
    public MemberSignInResponseDto signIn(MemberSignInRequestDto dto) {

        Member member = memberRepository.findByMemberId(dto.getMemberId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);
        }
        if (member.getRole() == Role.GUEST) {
            throw new CustomException(ErrorCode.NOT_VERIFY_EMAIL);
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
    public MemberDto updatePhone(String memberId, MemberOAuthUpdateDto dto) {
        Member member = memberRepository.findByMemberId(memberId)
            .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));
        member.updatePhone(dto.getPhone());

        return MemberDto.fromEntity(memberRepository.save(member));
    }

    @Transactional
    public void update(String memberId, MemberUpdateDto dto) {
        Member member = memberRepository.findByMemberId(memberId)
            .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));

        member.update(dto, passwordEncoder);
        memberRepository.save(member);
    }

    public void logout(String memberId, HttpServletRequest request,HttpServletResponse response) {
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
            throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);
        }

        member.withdrawal(LocalDateTime.now());
        memberRepository.save(member);
    }

    // 이메일 중복 확인
    private void checkDuplicatedEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }
    }

    // 닉네임 중복 확인
    private void checkDuplicatedMemberId(String memberId) {
        if (memberRepository.existsByMemberId(memberId)) {
            throw new CustomException(ErrorCode.DUPLICATED_MEMBER_ID);
        }
    }
}
