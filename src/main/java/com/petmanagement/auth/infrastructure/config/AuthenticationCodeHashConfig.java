package com.petmanagement.auth.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.function.BiPredicate;
import java.util.function.UnaryOperator;

@Configuration
class AuthenticationCodeHashConfig {

    @Bean
    PasswordEncoder authenticationCodeEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UnaryOperator<String> authenticationCodeHasher(PasswordEncoder authenticationCodeEncoder) {
        return authenticationCodeEncoder::encode;
    }

    @Bean
    BiPredicate<String, String> authenticationCodeVerifier(PasswordEncoder authenticationCodeEncoder) {
        return authenticationCodeEncoder::matches;
    }

}
