package com.harhay.poodle.rest;

import com.harhay.poodle.domain.AuthRequest;
import com.harhay.poodle.domain.AuthResponse;
import com.harhay.poodle.domain.User;
import com.harhay.poodle.service.TokenService;
import com.harhay.poodle.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/login", produces = APPLICATION_JSON_VALUE)
public class LoginController {

    private final TokenService tokenService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDetailsService userService;

    @PostMapping
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody @Valid AuthRequest authRequest) {
        return userService.findByUsername(authRequest.getUsername())
            .filter(userDetails -> passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword()))
            .map(userDetails -> ResponseEntity.ok(new AuthResponse(tokenService.generateToken((User) userDetails))))
            .switchIfEmpty(Mono.just(ResponseEntity.status(UNAUTHORIZED).build()));
    }
}
