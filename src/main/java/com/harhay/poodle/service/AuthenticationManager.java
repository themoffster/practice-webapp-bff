package com.harhay.poodle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final TokenService tokenService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String username = tokenService.getUsernameFromToken(authToken);
        return Mono.just(tokenService.validateToken(authToken))
            .filter(valid -> valid)
            .switchIfEmpty(Mono.empty())
            .map(validToken -> {
                List<SimpleGrantedAuthority> authorities = tokenService.getAllAuthoritiesFromToken(authToken);
                return new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities
                );
            });
    }
}
