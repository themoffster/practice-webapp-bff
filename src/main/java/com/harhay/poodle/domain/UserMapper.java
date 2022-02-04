package com.harhay.poodle.domain;

import com.harhay.poodle.data.UserEntity;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@UtilityClass
public class UserMapper {

    public static User map(UserEntity entity) {
        return User.builder()
            .username(entity.getUsername())
            .password(entity.getPassword())
            .isEnabled(entity.isEnabled())
            .isCredentialsNonExpired(entity.isCredentialsNonExpired())
            .isAccountNonLocked(entity.isAccountNonLocked())
            .isAccountNonExpired(entity.isCredentialsNonExpired())
            .authorities(mapAuthorities(entity.getAuthorities()))
            .build();
    }

    private Set<GrantedAuthority> mapAuthorities(Collection<String> authorities) {
        return authorities.stream()
            .map(authorityString -> new SimpleGrantedAuthority("ROLE_" + authorityString))
            .collect(toSet());
    }
}
