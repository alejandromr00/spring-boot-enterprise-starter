package com.nexora.ai.service;

import com.nexora.ai.entity.UserEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {

    public UserEntity requireUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken
                || !(authentication.getPrincipal() instanceof UserEntity user)
                || user.getId() == null) {
            throw new AuthenticationCredentialsNotFoundException("An authenticated user is required");
        }
        return user;
    }

    public Long requireId() {
        return requireUser().getId();
    }
}
