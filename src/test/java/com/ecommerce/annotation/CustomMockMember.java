package com.ecommerce.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomMockMemberSecurityContextFactory.class)
public @interface CustomMockMember {

    String memberId() default "qwer12";

    String email() default "qwer12@email.com";

    String role() default "USER";
}
