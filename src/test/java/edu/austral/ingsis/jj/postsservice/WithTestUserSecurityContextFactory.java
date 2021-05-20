package edu.austral.ingsis.jj.postsservice;

import edu.austral.ingsis.jj.postsservice.model.UserInfo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;
import java.util.Date;

public class WithTestUserSecurityContextFactory implements WithSecurityContextFactory<WithTestUser> {

    @Value("${JJ_SECRET}")
    private String jwtSecret;

    @Value("${JJ_EXPIRATION}")
    private int jwtExpirationMs;

        @Override
        public SecurityContext createSecurityContext(WithTestUser customUser) {
            SecurityContext context = SecurityContextHolder.createEmptyContext();

            UserInfo principal = UserInfo.builder()
                    .email(customUser.email())
                    .role(customUser.role())
                    .lastName(customUser.lastName())
                    .firstName(customUser.firstName())
                    .username(customUser.username())
                    .userId(customUser.userId())
                    .build();

            String jwt = Jwts.builder()
                    .setSubject(customUser.username())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();

            Authentication auth = new UsernamePasswordAuthenticationToken(principal, jwt, Collections.singletonList(new SimpleGrantedAuthority(customUser.role())));
            context.setAuthentication(auth);
            return context;
        }
}
