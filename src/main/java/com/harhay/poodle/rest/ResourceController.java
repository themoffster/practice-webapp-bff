package com.harhay.poodle.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ResourceController {

    @GetMapping("/resource/therapist")
    @PreAuthorize("hasRole('THERAPIST')")
    public Mono<ResponseEntity<String>> therapist() {
        return Mono.just(ResponseEntity.ok("Content for therapist"));
    }

    @GetMapping("/resource/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> admin() {
        return Mono.just(ResponseEntity.ok("Content for admin"));
    }

    @GetMapping("/resource/therapist-or-admin")
    @PreAuthorize("hasRole('THERAPIST') or hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> therapistOrAdmin() {
        return Mono.just(ResponseEntity.ok("Content for therapist or admin"));
    }
}
