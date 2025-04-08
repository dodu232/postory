package org.example.postory.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor

public class SecurityConfig {
    private final UserDetailsService userDetailsService;
}
