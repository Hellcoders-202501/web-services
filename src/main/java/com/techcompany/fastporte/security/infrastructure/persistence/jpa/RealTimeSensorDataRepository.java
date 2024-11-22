package com.techcompany.fastporte.security.infrastructure.persistence.jpa;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.RealTimeSensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RealTimeSensorDataRepository extends JpaRepository<RealTimeSensorData, Long> {
    //List<RealTimeSensorData> findTop30ByTripIdOrderByTimestampDesc(Long tripId);

    @Query("SELECT r FROM RealTimeSensorData r WHERE r.tripId = :tripId AND r.timestamp >= :timestampThreshold ORDER BY r.timestamp DESC")
    List<RealTimeSensorData> findRecentRecordsByTripId(
            @Param("tripId") Long tripId,
            @Param("timestampThreshold") LocalDateTime timestampThreshold
    );
}
