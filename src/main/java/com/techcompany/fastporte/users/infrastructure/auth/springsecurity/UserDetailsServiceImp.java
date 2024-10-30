package com.techcompany.fastporte.users.infrastructure.auth.springsecurity;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Supervisor;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.User;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.UserDetailsImp;
import com.techcompany.fastporte.users.domain.model.exceptions.InvalidCredentialsException;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.DriverRepository;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.SupervisorRepository;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final SupervisorRepository supervisorRepository;

    public UserDetailsServiceImp(UserRepository userRepository, DriverRepository driverRepository, SupervisorRepository supervisorRepository) {
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.supervisorRepository = supervisorRepository;
    }

    /// This method is used to load user by username or email.
    /// In this case, we are using email as username.
    @Override
    public UserDetailsImp loadUserByUsername(String username) {

        //Find user by email
        Optional<User> user = userRepository.findByEmail(username);

        //Find user by username
        //Optional<User> driver = userRepository.findByUsername(username);

        if (user.isPresent()) {

            //Username or email:
            String _username = user.get().getEmail();
            //String _username = user.get().getUsername();

            String _password = user.get().getPassword();

            // Mapear roles a objetos GrantedAuthority
            List<GrantedAuthority> authorities = user.get().getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                    .collect(Collectors.toList());

            // If user is a driver
            Optional<Driver> _driver = driverRepository.findByUserId(user.get().getId());
            if (_driver.isPresent()) {
                return new UserDetailsImp(_username, _password, _driver.get().getId(), authorities);
            }

            // If user is a supervisor
            Optional<Supervisor> _supervisor = supervisorRepository.findByUserId(user.get().getId());
            if (_supervisor.isPresent()) {
                return new UserDetailsImp(_username, _password, _supervisor.get().getId(), authorities);
            }

            //Set first argument if you want to use email as username
            //return new UserDetailsImp(user.get().getEmail(), user.get().getPassword(), user.get().getId(), authorities);
            throw new InvalidCredentialsException();

        } else {
            throw new InvalidCredentialsException();
        }
    }
}
