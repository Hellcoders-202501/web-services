package com.techcompany.fastporte.users.infrastructure.auth.springsecurity;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Supervisor;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.UserDetailsImp;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.DriverRepository;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.RoleRepository;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.SupervisorRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final DriverRepository driverRepository;
    private final SupervisorRepository supervisorRepository;

    public UserDetailsServiceImp(DriverRepository driverRepository, SupervisorRepository supervisorRepository, RoleRepository roleRepository) {
        this.driverRepository = driverRepository;
        this.supervisorRepository = supervisorRepository;
    }

    @Override
    public UserDetailsImp loadUserByUsername(String username) {
        Optional<Driver> driver = driverRepository.findByUser_Username(username);
        if (driver.isPresent()) {

            System.out.println("username: " + username + " encontrado en la tabla driver");
            System.out.println("Roles: " + driver.get().getUser().getRoles().stream()
                    .map(role -> role.getRoleName().name())
                    .collect(Collectors.joining(", ")));

            // Mapear roles a objetos GrantedAuthority
            List<GrantedAuthority> authorities = driver.get().getUser().getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                    .collect(Collectors.toList());

            return new UserDetailsImp(driver.get().getUser().getUsername(), driver.get().getUser().getPassword(), driver.get().getId(), authorities);

            /*return User.builder()
                    .username(driver.get().getUser().getUsername())
                    .password(driver.get().getUser().getPassword())
                    .authorities(authorities)
                    .build();*/
        }

        System.out.println("username: " + username + "Not found in driver table");

        Optional<Supervisor> supervisor = supervisorRepository.findByUser_Username(username);
        if (supervisor.isPresent()) {

            System.out.println("username: " + username + " encontrado en la tabla supervisor");
            System.out.println("Roles: " + supervisor.get().getUser().getRoles().stream()
                    .map(role -> role.getRoleName().name())
                    .collect(Collectors.joining(", ")));

            // Mapear roles a objetos GrantedAuthority
            List<GrantedAuthority> authorities = supervisor.get().getUser().getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                    .collect(Collectors.toList());

            return new UserDetailsImp(supervisor.get().getUser().getUsername(), supervisor.get().getUser().getPassword(), supervisor.get().getId(), authorities);

            /*return User.builder()
                    .username(supervisor.get().getUser().getUsername())
                    .password(supervisor.get().getUser().getPassword())
                    .authorities(authorities)
                    .build();*/
        }

        System.out.println("username: " + username + "Not found in supervisor table");

        return new UserDetailsImp("username", "password", 0L, List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}