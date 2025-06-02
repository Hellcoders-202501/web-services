package com.techcompany.fastporte.users.application.internal.commandservices;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.UserDetailsImp;
import com.techcompany.fastporte.users.domain.model.commands.auth.AuthenticateAccountCommand;
import com.techcompany.fastporte.users.domain.services.auth.AuthenticationCommandService;
import com.techcompany.fastporte.users.infrastructure.auth.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationCommandServiceImp implements AuthenticationCommandService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthenticationCommandServiceImp(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public String handle(AuthenticateAccountCommand command) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(command.email(), command.password()));
        final UserDetailsImp userDetails = (UserDetailsImp) userDetailsService.loadUserByUsername(command.email());

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("");

        return jwtUtil.generateToken(userDetails.getUsername(), userDetails.getUserId(), role);
    }
}