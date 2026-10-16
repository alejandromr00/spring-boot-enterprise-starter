package com.nexora.ai.service;

import com.nexora.ai.entity.UserEntity;
import com.nexora.ai.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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
        UserEntity user = userRepository
                .findByUsernameIgnoreCaseOrEmailIgnoreCase(identifier, identifier)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));


        System.out.println();
        System.out.println(authoritiesOf(user));
        System.out.println();

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authoritiesOf(user))
                .accountLocked(Boolean.TRUE.equals(user.getLocked()))
                .disabled(Boolean.TRUE.equals(user.getDisabled()))
                .build();
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
