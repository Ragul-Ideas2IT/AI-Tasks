package com.connectinghands.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    long id() default 1L;
    String username() default "user";
    String[] roles() default {"USER"};
    String email() default "user@example.com";
}

class CustomUserPrincipal {
    private final long id;
    private final String username;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserPrincipal(long id, String username, String email, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.authorities = authorities;
    }

    public long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
}

class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        
        Collection<GrantedAuthority> authorities = Arrays.stream(annotation.roles())
            .map(role -> "ROLE_" + role)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
        
        CustomUserPrincipal principal = new CustomUserPrincipal(
            annotation.id(),
            annotation.username(),
            annotation.email(),
            authorities
        );
        
        Authentication auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
            principal,
            "password",
            authorities
        );
        
        context.setAuthentication(auth);
        return context;
    }
} 