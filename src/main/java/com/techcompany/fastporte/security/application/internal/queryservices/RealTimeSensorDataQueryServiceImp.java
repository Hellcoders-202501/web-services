package com.techcompany.fastporte.security.application.internal.queryservices;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.RealTimeSensorData;
import com.techcompany.fastporte.security.domain.model.queries.GetRealTimeSensorDataQuery;
import com.techcompany.fastporte.security.domain.services.RealTimeSensorDataQueryService;
import com.techcompany.fastporte.security.infrastructure.persistence.jpa.RealTimeSensorDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service

public class RealTimeSensorDataQueryServiceImp implements RealTimeSensorDataQueryService {

    private final RealTimeSensorDataRepository realTimeSensorDataRepository;

    public RealTimeSensorDataQueryServiceImp(RealTimeSensorDataRepository realTimeSensorDataRepository) {
        this.realTimeSensorDataRepository = realTimeSensorDataRepository;
    }

    @Override
    public List<RealTimeSensorData> handle(GetRealTimeSensorDataQuery query) {

        // Obtén la zona horaria de Perú
        ZoneId peruZone = ZoneId.of("America/Lima");

        // Obtén la hora actual en Perú
        ZonedDateTime peruTime = ZonedDateTime.now(peruZone);

        // Calcula el timestamp de hace 30 segundos
        LocalDateTime now = peruTime.toLocalDateTime();
        LocalDateTime thirtySecondsAgo = now.minusSeconds(30);

        return realTimeSensorDataRepository.findRecentRecordsByTripId(query.tripId(), thirtySecondsAgo);
    }
}
