
package com.nexora.ai.service;

import com.nexora.ai.entity.UserEntity;
import com.nexora.ai.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

    private static final String ROLE_PREFIX = "ROLE_";

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) {
        // UserEntity implementa UserDetails: se devuelve tal cual, sin convertir a User de Spring
        return userRepository
                .findByUsernameIgnoreCaseOrEmailIgnoreCase(identifier, identifier)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));
    }

    private static Set<GrantedAuthority> authoritiesOf(UserEntity user) {
        return user.getRoles().stream()
                .flatMap(role -> Stream.concat(
                        Stream.of(ROLE_PREFIX + role.getName()),
                        role.getPermissions().stream().map(Enum::name)
                ))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());
    }
}
