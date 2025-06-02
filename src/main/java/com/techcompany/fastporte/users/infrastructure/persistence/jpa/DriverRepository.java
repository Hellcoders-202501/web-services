package com.techcompany.fastporte.users.infrastructure.persistence.jpa;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

 @Repository
public interface DriverRepository  extends JpaRepository<Driver, Long> {

    List<Driver> findAllByUser_Name(String name);
    Optional<Driver> findByUser_Email (String email);
    //Optional<Driver> findByUser_Username (String email);
    List<Driver> findAllByIdIn(List<Long> ids);
    //List<Driver> findAllBySupervisor_Id(Long clientId);
    Optional<Driver> findByUserId(Long userId);

    List<Driver> findTop3ByOrderByRatingDesc();
}
