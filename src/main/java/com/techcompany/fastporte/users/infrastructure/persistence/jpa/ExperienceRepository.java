package com.techcompany.fastporte.users.infrastructure.persistence.jpa;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    void deleteExperienceById(Long id);
    List<Experience> findAllExperienceByDriver_Id(Long driverId);
}
