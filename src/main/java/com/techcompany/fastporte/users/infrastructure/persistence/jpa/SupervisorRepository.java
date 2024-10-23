package com.techcompany.fastporte.users.infrastructure.persistence.jpa;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupervisorRepository extends JpaRepository<Supervisor, Long> {

    Optional<List<Supervisor>> findAllByUser_Name(String name);
    Optional<Supervisor> findByUser_Email(String email);
    Optional<Supervisor> findByUser_Username(String username);

    List<Long> findAllDriversIdById(Long id);
}
