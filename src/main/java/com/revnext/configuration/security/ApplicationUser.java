package com.revnext.configuration.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;


@Data
@EqualsAndHashCode
@Builder
public class ApplicationUser implements UserDetails {

    final private Collection<? extends GrantedAuthority> authorities = Collections.emptyList();
    private UUID id;
    private String username;
    private String email;

    @JsonIgnore
    private String password;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
