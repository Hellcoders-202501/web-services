package com.techcompany.fastporte.users.infrastructure.persistence.jpa;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findAllByUser_Name(String name);
    Optional<Client> findByUser_Email(String email);
    Optional<Client> findByUser_Username(String username);

    List<Long> findAllDriversIdById(Long id);
    Optional<Client> findByUserId(Long userId);
}
