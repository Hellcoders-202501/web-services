package com.techcompany.fastporte.users.application.internal.queryservices;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Comment;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.CommentRepository;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.*;
import com.techcompany.fastporte.users.domain.model.queries.driver.*;
import com.techcompany.fastporte.users.domain.services.driver.DriverQueryService;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DriverQueryServiceImp implements DriverQueryService {

    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final ExperienceRepository experienceRepository;
    private final CommentRepository commentRepository;
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountTypeRepository bankAccountTypeRepository;

    public DriverQueryServiceImp(DriverRepository driverRepository, VehicleRepository vehicleRepository, ExperienceRepository experienceRepository, CommentRepository commentRepository, BankAccountRepository bankAccountRepository, BankAccountTypeRepository bankAccountTypeRepository) {
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.experienceRepository = experienceRepository;
        this.commentRepository = commentRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountTypeRepository = bankAccountTypeRepository;
    }

    @Override
    public Optional<Driver> handle(GetDriverByIdQuery query) {
        return driverRepository.findById(query.id());
    }

    @Override
    public List<Driver> handle(GetAllDriversQuery query) {
        return driverRepository.findAll();
    }

    @Override
    public List<Driver> handle(GetAllDriversByIdInList query) {
        return driverRepository.findAllById(query.ids());
    }

    @Override
    public Optional<Driver> handle(GetDriverPrivateProfileQuery query) {
        return driverRepository.findById(query.id());
    }

    @Override
    public Optional<Driver> handle(GetDriverPublicProfileQuery query) {
        return driverRepository.findById(query.id());
    }

    @Override
    public Boolean handle(CheckDriverExistsByIdQuery query) {
        return driverRepository.existsById(query.driverId());
    }

    @Override
    public List<Experience> handle(GetAllExperiencesByDriverIdQuery query) {
        return experienceRepository.findAllExperienceByDriver_Id(query.driverId());
    }

    @Override
    public List<Vehicle> handle(GetAllVehiclesByDriverIdQuery query) {
        return vehicleRepository.findAllByDriver_Id(query.driverId());
    }

    @Override
    public List<Comment> handle(GetAllCommentsByDriverIdQuery query) {
        return commentRepository.findAllByTrip_Request_Contract_Driver_Id(query.driverId());
    }

    @Override
    public List<Driver> handle(GetMostRankedDriversQuery query) {
        return driverRepository.findTop3ByOrderByRatingDesc();
    }

    @Override
    public Optional<BankAccount> handle(GetBankAccountQuery query) {
        return bankAccountRepository.findByDriver_id(query.driverId());
    }

    @Override
    public List<BankAccountType> handle(GetAllBankAccountTypesQuery query) {
        return bankAccountTypeRepository.findAll();
    }
}
