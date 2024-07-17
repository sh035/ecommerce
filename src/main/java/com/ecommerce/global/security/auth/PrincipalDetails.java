package com.ecommerce.global.security.auth;

import com.ecommerce.member.domain.entity.Member;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@Builder
public class PrincipalDetails implements OAuth2User, UserDetails {
  private Member member;
  private Map<String, Object> attributes;

  // 일반 로그인
  public PrincipalDetails(Member member) {
    this.member = member;
  }
  // OAuth2 로그인
  public PrincipalDetails(Member member, Map<String, Object> attributes) {
    this.member = member;
    this.attributes = attributes;
  }


  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> collection = new ArrayList<>();

    collection.add((GrantedAuthority) () -> member.getRole().toString());

    return collection;
  }

  @Override
  public String getName() {
    return member.getNickname();
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public String getUsername() {
    return member.getEmail();
  }

  @Override
  public String getPassword() {
    return member.getPassword();
  }
}
