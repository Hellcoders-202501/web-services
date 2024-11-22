package com.techcompany.fastporte.users.application.internal.commandservices;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Role;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.SensorCode;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Supervisor;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.User;
import com.techcompany.fastporte.users.domain.model.aggregates.enums.RoleName;
import com.techcompany.fastporte.users.domain.model.commands.supervisor.DeleteSupervisorCommand;
import com.techcompany.fastporte.users.domain.model.commands.supervisor.RegisterSupervisorCommand;
import com.techcompany.fastporte.users.domain.model.commands.supervisor.UpdateSupervisorInformationCommand;
import com.techcompany.fastporte.users.domain.model.exceptions.*;
import com.techcompany.fastporte.users.domain.services.supervisor.SupervisorCommandService;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.RoleRepository;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.SensorCodeRepository;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.SupervisorRepository;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Transactional
@Service
public class SupervisorCommandServiceImp implements SupervisorCommandService {

    private final UserRepository userRepository;
    private final SupervisorRepository supervisorRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final SensorCodeRepository sensorCodeRepository;

    public SupervisorCommandServiceImp(UserRepository userRepository, SupervisorRepository supervisorRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, SensorCodeRepository sensorCodeRepository) {
        this.userRepository = userRepository;
        this.supervisorRepository = supervisorRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.sensorCodeRepository = sensorCodeRepository;
    }

    @Override
    public Optional<Supervisor> handle(RegisterSupervisorCommand command) {

        /// Search if the email already exists
        if (userRepository.existsByEmail(command.email())) {
            System.out.println("Email already exists");
            throw new EmailAlreadyExistsException(command.email());
        }

        /// Search if the sensor code already exists
        if (sensorCodeRepository.existsByCode(command.sensorCode())) {
            System.out.println("Sensor code already exists");
            throw new SensorCodeAlreadyExistsException(command.sensorCode());
        }

        /// Create the Supervisor object
        Supervisor supervisor = new Supervisor(command);

        /// Search if the supervisor exists
        User user = supervisor.getUser();

        /// Search if the supervisor role exists
        Role supervisorRole = roleRepository.findByRoleName(RoleName.ROLE_SUPERVISOR)
                .orElseThrow(() -> new RoleNotFoundException(RoleName.ROLE_SUPERVISOR.toString()));

        /// Assign the role to the user
        user.setRoles(Set.of(supervisorRole));

        /// Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(supervisor.getUser());

        /// Assign the user to the supervisor
        supervisor.setUser(savedUser);
        Supervisor savedSupervisor = supervisorRepository.save(supervisor);

        /// Save the sensor code five times with the field driver_id in blank
        String code = command.sensorCode().toUpperCase();
        for (int i = 0; i < 5; i++) {
            SensorCode sensorCode = new SensorCode(code, savedSupervisor);
            sensorCodeRepository.save(sensorCode);
        }

        return Optional.of(savedSupervisor);
    }

    @Override
    public Optional<Supervisor> handle(UpdateSupervisorInformationCommand command) {
        Supervisor supervisor = supervisorRepository.findById(command.id())
                .orElseThrow(() -> new SupervisorNotFoundException(command.id()));

        User user = userRepository.findById(supervisor.getUser().getId())
                .orElseThrow(() -> new DriverNotFoundException(command.id()));

        user.setName(command.name());
        user.setFirstLastName(command.firstLastName());
        user.setSecondLastName(command.secondLastName());
        user.setEmail(command.email());
        user.setPhone(command.phone());

        return Optional.of(supervisorRepository.save(supervisor));
    }

    @Override
    public void handle(DeleteSupervisorCommand command) {
        supervisorRepository.deleteById(command.supervisorId());
    }
}
