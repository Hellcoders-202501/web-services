package com.techcompany.fastporte.users.application.internal.commandservices;

import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.ServiceRepository;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.*;
import com.techcompany.fastporte.users.domain.model.aggregates.enums.RoleName;
import com.techcompany.fastporte.users.domain.model.commands.driver.*;
import com.techcompany.fastporte.users.domain.model.exceptions.*;
import com.techcompany.fastporte.users.domain.services.driver.DriverCommandService;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Transactional
@Service
public class DriverCommandServiceImp implements DriverCommandService {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final RoleRepository roleRepository;
    private final ExperienceRepository experienceRepository;
    private final VehicleRepository vehicleRepository;
    private final ServiceRepository serviceRepository;
    private final PasswordEncoder passwordEncoder;
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountTypeRepository bankAccountTypeRepository;

    public DriverCommandServiceImp(UserRepository userRepository, DriverRepository driverRepository, RoleRepository roleRepository, ExperienceRepository experienceRepository, VehicleRepository vehicleRepository, ServiceRepository serviceRepository, PasswordEncoder passwordEncoder, BankAccountRepository bankAccountRepository, BankAccountTypeRepository bankAccountTypeRepository) {
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.roleRepository = roleRepository;
        this.experienceRepository = experienceRepository;
        this.vehicleRepository = vehicleRepository;
        this.serviceRepository = serviceRepository;
        this.passwordEncoder = passwordEncoder;
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountTypeRepository = bankAccountTypeRepository;
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
        user.setPhone(command.phone());
        user.setDescription(command.description());

        return Optional.of(driverRepository.save(driver));
    }

    @Override
    public void handle(DeleteDriverCommand command) {
        driverRepository.deleteById(command.driverId());
    }

    @Override
    public Optional<Experience> handle(AddDriverExperienceCommand command){

        Driver driver = driverRepository.findById(command.id())
                .orElseThrow(() -> new DriverNotFoundException(command.id()));

        Experience experience = new Experience(command.job(), BigDecimal.valueOf(command.duration()), driver);

        return Optional.of(experienceRepository.save(experience));
    }

    @Override
    public void handle(DeleteDriverExperienceCommand command) {
            experienceRepository.deleteById(command.experienceId());
    }

    @Override
    public Optional<Vehicle> handle(AddDriverVehicleCommand command) {
        Driver driver = driverRepository.findById(command.driverId())
                .orElseThrow(() -> new DriverNotFoundException(command.driverId()));

        com.techcompany.fastporte.trips.domain.model.aggregates.entities.Service service = new com.techcompany.fastporte.trips.domain.model.aggregates.entities.Service();
        service = serviceRepository.findById(command.serviceId()).get();

        Vehicle vehicle = new Vehicle(command.brand(), command.imageUrl());
        vehicle.setDriver(driver);
        vehicle.getServices().add(service);
        driver.getVehicles().add(vehicle);
        driverRepository.save(driver);

        return Optional.of(vehicle);
    }

    @Override
    public void handle(DeleteDriverVehicleCommand command) {
        vehicleRepository.deleteById(command.vehicleId());
    }

    @Override
    public Optional<BankAccount> handle(AddBankAccountCommand command) {

        Driver driver = driverRepository.findById(command.driverId()).get();
        BankAccountType type = bankAccountTypeRepository.findById(command.accountTypeId()).get();
        BankAccount bankAccount = new BankAccount(driver, command.bankName(), command.accountNumber(), type);

        return Optional.of(bankAccountRepository.save(bankAccount));
    }

    @Override
    public void handle(DeleteBankAccountCommand command) {

        Optional<BankAccount> bankAccountOpt = bankAccountRepository.findById(command.id());

        if (bankAccountOpt.isEmpty()) {
            throw new RuntimeException("Cuenta bancaria no encontrada.");
        }

        BankAccount bankAccount = bankAccountOpt.get();
        Driver driver = bankAccount.getDriver();

        // Romper la relación en memoria y persistirla
        driver.setBankAccount(null);
        bankAccount.setDriver(null);

        // Persistir cambio en el lado del Driver
        driverRepository.save(driver);

        // Eliminar la cuenta bancaria
        bankAccountRepository.delete(bankAccount);
    }

    @Override
    public void handle(UpdateBankAccountCommand command) {

        Optional<BankAccount> bankAccount = bankAccountRepository.findById(command.id());

        if (bankAccount.isEmpty()) {
            throw new RuntimeException("Cuenta bancaria no encontrada.");
        }

        BankAccount bankAccount1 = bankAccount.get();
        BankAccountType type = bankAccountTypeRepository.findById(command.accountTypeId()).get();

        bankAccount1.setBankName(command.bankName());
        bankAccount1.setAccountNumber(command.accountNumber());
        bankAccount1.setAccountType(type);

        bankAccountRepository.save(bankAccount1);
    }
}
