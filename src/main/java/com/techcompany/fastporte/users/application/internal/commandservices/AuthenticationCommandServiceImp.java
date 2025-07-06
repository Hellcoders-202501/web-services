package com.techcompany.fastporte.users.application.internal.commandservices;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.User;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.UserDetailsImp;
import com.techcompany.fastporte.users.domain.model.commands.auth.AuthenticateAccountCommand;
import com.techcompany.fastporte.users.domain.model.commands.auth.UpdatePasswordCommand;
import com.techcompany.fastporte.users.domain.services.auth.AuthenticationCommandService;
import com.techcompany.fastporte.users.infrastructure.auth.jwt.JwtUtil;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AuthenticationCommandServiceImp implements AuthenticationCommandService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthenticationCommandServiceImp(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
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

    @Override
    public void handle(UpdatePasswordCommand command) {

        Optional<User> user = userRepository.findByEmail(command.email());

        if (user.isEmpty()){
            throw new RuntimeException("Usuario con email '"+ command.email() +"' no encontrado");
        }

        User userU = user.get();
        userU.setPassword(passwordEncoder.encode(command.newPassword()));
        userRepository.save(userU);
    }
}