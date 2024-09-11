package com.ecommerce.global.security.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class PrincipalDetails implements UserDetails, OAuth2User {

    private String email;
    private String memberId;
    private String role;
    private Map<String, Object> attributes;

    // 일반 로그인
    public PrincipalDetails(String memberId, String email, String role) {
        this.memberId = memberId;
        this.email = email;
        this.role = role;
    }

    // OAuth2 로그인
    public PrincipalDetails(String memberId, String email, String role,
        Map<String, Object> attributes) {
        this.memberId = memberId;
        this.email = email;
        this.role = role;
        this.attributes = attributes;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add((GrantedAuthority) () -> role);

        return collection;
    }

    @Override
    public String getName() {
        return memberId;
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
        return email;
    }

    @Override
    public String getPassword() {
        return null;
    }
}
