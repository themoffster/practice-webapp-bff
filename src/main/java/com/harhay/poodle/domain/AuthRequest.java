package com.harhay.poodle.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@RequiredArgsConstructor
public class AuthRequest {

    private final @NotEmpty String username;
    private final @NotEmpty String password;
}
