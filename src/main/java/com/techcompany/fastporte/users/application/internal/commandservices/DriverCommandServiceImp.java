package com.techcompany.fastporte.users.application.internal.commandservices;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Role;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Supervisor;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.User;
import com.techcompany.fastporte.users.domain.model.aggregates.enums.RoleName;
import com.techcompany.fastporte.users.domain.model.commands.driver.DeleteDriverCommand;
import com.techcompany.fastporte.users.domain.model.commands.driver.RegisterDriverCommand;
import com.techcompany.fastporte.users.domain.model.commands.driver.UpdateDriverInformationCommand;
import com.techcompany.fastporte.users.domain.model.exceptions.DriverNotFoundException;
import com.techcompany.fastporte.users.domain.model.exceptions.EmailAlreadyExistsException;
import com.techcompany.fastporte.users.domain.model.exceptions.RoleNotFoundException;
import com.techcompany.fastporte.users.domain.model.exceptions.SupervisorNotFoundException;
import com.techcompany.fastporte.users.domain.services.driver.DriverCommandService;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.DriverRepository;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.RoleRepository;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.SupervisorRepository;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Transactional
@Service
public class DriverCommandServiceImp implements DriverCommandService {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final SupervisorRepository supervisorRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DriverCommandServiceImp(UserRepository userRepository, DriverRepository driverRepository, SupervisorRepository supervisorRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.supervisorRepository = supervisorRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<Driver> handle(RegisterDriverCommand command) {

        //Buscar si el username/*email* existe
        if (userRepository.existsByEmail(command.email())) {
            System.out.println("Email already exists");
            throw new EmailAlreadyExistsException(command.email());
        }

        Supervisor supervisor = null;

        //Buscar si el supervisor existe
        if (command.supervisorId() != null) {
            supervisor = supervisorRepository.findById(command.supervisorId())
                    .orElseThrow(() -> new SupervisorNotFoundException(command.supervisorId()));
        }

        // Mapear el objeto DriverRegisterDto a un objeto Driver
        Driver driver = new Driver(command, supervisor);

        // Persistir el objeto User primero
        User user = driver.getUser();

        // Recuperar el rol desde la base de datos
        Role driverRole = roleRepository.findByRoleName(RoleName.ROLE_DRIVER)
                .orElseThrow(() -> new RoleNotFoundException(RoleName.ROLE_DRIVER.toString()));

        // Asignar el rol al usuario
        user.setRoles(Set.of(driverRole));

        // Cifrar la contraseña del usuario antes de guardarlo
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(driver.getUser());

        // Asignar el objeto User persistido al objeto Driver
        driver.setUser(savedUser);
        Driver savedDriver = driverRepository.save(driver);

        //return driverMapper.driverToResponseDto(savedDriver);
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
        user.setPassword(passwordEncoder.encode(command.password()));
        user.setPhone(command.phone());

        return Optional.of(driverRepository.save(driver));
    }

    @Override
    public void handle(DeleteDriverCommand command) {
        driverRepository.deleteById(command.driverId());
    }
}
