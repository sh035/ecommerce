package com.ecommerce.global.security.auth;

import com.ecommerce.member.domain.entity.Member;
import com.ecommerce.member.domain.enums.Role;
import com.ecommerce.member.repository.MemberRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String memberId = provider + "_" + UUID.randomUUID().toString().substring(0, 6);
        String password = passwordEncoder.encode(
            "비밀번호" + UUID.randomUUID().toString().substring(0, 6));
        String email = oAuth2User.getAttribute("email");

        Member member = memberRepository.findByEmail(email)
            .orElseGet(() -> signUpMember(memberId, email, password, provider, providerId));

        return new PrincipalDetails(member.getMemberId(), member.getEmail(),
            member.getRole().toString(), oAuth2User.getAttributes());
    }

    private Member signUpMember(String memberId, String email, String password, String provider,
        String providerId) {
        Member user = Member.builder()
            .memberId(memberId)
            .email(email)
            .password(password)
            .authProvider(provider)
            .providerId(providerId)
            .role(Role.USER)
            .build();

        return memberRepository.save(user);
    }
}
