package com.harhay.poodle.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

import static com.harhay.poodle.domain.Role.ADMIN;
import static com.harhay.poodle.domain.Role.THERAPIST;

@Entity
@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    @Column(name = "account_non_expired")
    private boolean isAccountNonExpired;

    @Column(name = "credentials_non_expired")
    private boolean isCredentialsNonExpired;

    @Column(name = "enabled")
    private boolean isEnabled;

    @Column(name = "password")
    private String password;

    // TODO
    public List<String> getAuthorities() {
        return List.of(THERAPIST.name(), ADMIN.name());
    }
}
