package edu.austral.ingsis.jj.postsservice;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithTestUserSecurityContextFactory.class)
public @interface WithTestUser {

    String username() default "test";
    String password() default "password";
    String userId() default "123";
    String email() default "test@gmail.com";
    String firstName() default "test";
    String lastName() default "test";
    String role() default "ROLE_USER";
}
