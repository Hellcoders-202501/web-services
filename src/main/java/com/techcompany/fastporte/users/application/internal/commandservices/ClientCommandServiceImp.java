package com.techcompany.fastporte.users.application.internal.commandservices;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Role;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Client;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.User;
import com.techcompany.fastporte.users.domain.model.aggregates.enums.RoleName;
import com.techcompany.fastporte.users.domain.model.commands.client.DeleteClientCommand;
import com.techcompany.fastporte.users.domain.model.commands.client.RegisterClientCommand;
import com.techcompany.fastporte.users.domain.model.commands.client.UpdateClientInformationCommand;
import com.techcompany.fastporte.users.domain.model.exceptions.*;
import com.techcompany.fastporte.users.domain.services.client.ClientCommandService;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.RoleRepository;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.ClientRepository;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Transactional
@Service
public class ClientCommandServiceImp implements ClientCommandService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientCommandServiceImp(UserRepository userRepository, ClientRepository clientRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<Client> handle(RegisterClientCommand command) {

        /// Search if the email already exists
        if (userRepository.existsByEmail(command.email())) {
            System.out.println("Email already exists");
            throw new EmailAlreadyExistsException(command.email());
        }

        /// Create the Client object
        Client client = new Client(command);

        /// Search if the client exists
        User user = client.getUser();

        /// Search if the client role exists
        Role clientRole = roleRepository.findByRoleName(RoleName.ROLE_CLIENT)
                .orElseThrow(() -> new RoleNotFoundException(RoleName.ROLE_CLIENT.toString()));

        /// Assign the role to the user
        user.setRoles(Set.of(clientRole));

        /// Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(client.getUser());

        /// Assign the user to the client
        client.setUser(savedUser);
        Client savedClient = clientRepository.save(client);

        return Optional.of(savedClient);
    }

    @Override
    public Optional<Client> handle(UpdateClientInformationCommand command) {
        Client client = clientRepository.findById(command.id())
                .orElseThrow(() -> new ClientNotFoundException(command.id()));

        User user = userRepository.findById(client.getUser().getId())
                .orElseThrow(() -> new DriverNotFoundException(command.id()));

        user.setName(command.name());
        user.setFirstLastName(command.firstLastName());
        user.setSecondLastName(command.secondLastName());
        user.setEmail(command.email());
        user.setPhone(command.phone());

        return Optional.of(clientRepository.save(client));
    }

    @Override
    public void handle(DeleteClientCommand command) {
        clientRepository.deleteById(command.clientId());
    }
}
