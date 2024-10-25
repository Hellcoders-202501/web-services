package com.techcompany.fastporte.users.domain.model.aggregates.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Map;

@Data
@AllArgsConstructor
public class UserDetailsImp implements UserDetails {

    private String username;
    private String password;
    private Long userId;
    private Collection<? extends GrantedAuthority> authorities;

    /*public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorityList =
                user.getRoles().stream().map(
                        role -> new SimpleGrantedAuthority(
                                role.getRoleName().name())).collect(Collectors.toList());
        return new UserDetailsImpl(
                user.getUsername(),
                user.getPassword(),
                authorityList);
    }*/

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

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

    public Map<String, String> getClaims() {
        return Map.of(
                "username", username
        );
    }
}