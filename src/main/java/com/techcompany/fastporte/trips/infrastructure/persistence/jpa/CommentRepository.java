package com.techcompany.fastporte.trips.infrastructure.persistence.jpa;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT AVG(c.rating) FROM Comment c WHERE c.trip.request.contract.driver.id = :driverId")
    Double calculateAverageRatingForDriver(@Param("driverId") Long driverId);

    List<Comment> findAllByTrip_Request_Contract_Driver_Id(Long id);

}
