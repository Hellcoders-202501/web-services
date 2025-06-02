package com.techcompany.fastporte.users.infrastructure.persistence.jpa;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //Optional<User> findByUsername(String email);
    Optional<User> findByEmail(String email);
    //Optional<User> findByUsernameOrEmail(String email, String email);
    //Boolean existsByUsername(String email);
    Boolean existsByEmail(String email);
}
