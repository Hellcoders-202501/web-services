package com.techcompany.fastporte.users.application.internal.commandservices;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.*;
import com.techcompany.fastporte.users.domain.model.aggregates.enums.RoleName;
import com.techcompany.fastporte.users.domain.model.commands.driver.DeleteDriverCommand;
import com.techcompany.fastporte.users.domain.model.commands.driver.RegisterDriverCommand;
import com.techcompany.fastporte.users.domain.model.commands.driver.UpdateDriverInformationCommand;
import com.techcompany.fastporte.users.domain.model.exceptions.*;
import com.techcompany.fastporte.users.domain.services.driver.DriverCommandService;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Service
public class DriverCommandServiceImp implements DriverCommandService {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DriverCommandServiceImp(UserRepository userRepository, DriverRepository driverRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<Driver> handle(RegisterDriverCommand command) {

        /// Search if the email already exists
        if (userRepository.existsByEmail(command.email())) {
            System.out.println("Email already exists");
            throw new EmailAlreadyExistsException(command.email());
        }

        /// Create the driver object
        Driver driver = new Driver(command);

        /// Create the user object
        User user = driver.getUser();

        /// Search the role driver
        Role driverRole = roleRepository.findByRoleName(RoleName.ROLE_DRIVER)
                .orElseThrow(() -> new RoleNotFoundException(RoleName.ROLE_DRIVER.toString()));

        /// Assign the role to the user
        user.setRoles(Set.of(driverRole));

        /// Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(driver.getUser());

        /// Assign the user to the driver
        driver.setUser(savedUser);
        Driver savedDriver = driverRepository.save(driver);

        return Optional.of(savedDriver);
    }

    @Override
    public Optional<Driver> handle(UpdateDriverInformationCommand command) {
        Driver driver = driverRepository.findById(command.id())
                .orElseThrow(() -> new DriverNotFoundException(command.id()));

        User user = userRepository.findById(driver.getUser().getId())
                .orElseThrow(() -> new DriverNotFoundException(command.id()));

        user.setName(command.name());
        user.setFirstLastName(command.firstLastName());
        user.setSecondLastName(command.secondLastName());
        user.setEmail(command.email());
        user.setPhone(command.phone());

        return Optional.of(driverRepository.save(driver));
    }

    @Override
    public void handle(DeleteDriverCommand command) {
        driverRepository.deleteById(command.driverId());
    }
}
