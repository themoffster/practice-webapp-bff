package com.harhay.poodle.service;

import com.harhay.poodle.data.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.harhay.poodle.domain.UserMapper.map;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository repository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        log.debug("Looking for user with username {}.", username);
        return Mono.just(map(Optional.of(repository.getUserEntitiesByUsername(username)).orElseThrow()));
    }
}
