package com.ecommerce.annotation;

import com.ecommerce.global.security.auth.PrincipalDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithCustomMockMemberSecurityContextFactory implements
    WithSecurityContextFactory<CustomMockMember>{

    @Override
    public SecurityContext createSecurityContext(CustomMockMember annotation) {

        String memberId = annotation.memberId();
        String email = annotation.email();
        String role = annotation.role();

        PrincipalDetails principalDetails = new PrincipalDetails(memberId, email, role);

        UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(
            principalDetails, "", principalDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(principal);

        return context;
    }
}
