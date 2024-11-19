package com.techcompany.fastporte.security.application.internal.commandservices;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.RealTimeSensorData;
import com.techcompany.fastporte.security.domain.model.commands.SaveRealTimeSensorDataCommand;
import com.techcompany.fastporte.security.domain.services.RealTimeSensorDataCommandService;
import com.techcompany.fastporte.security.infrastructure.persistence.jpa.RealTimeSensorDataRepository;
import com.techcompany.fastporte.shared.dtos.RealTimeSensorDataDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RealTimeSensorDataCommandServiceImp implements RealTimeSensorDataCommandService {

    private final RealTimeSensorDataRepository realTimeSensorDataRepository;

    public RealTimeSensorDataCommandServiceImp(RealTimeSensorDataRepository realTimeSensorDataRepository) {
        this.realTimeSensorDataRepository = realTimeSensorDataRepository;
    }

    @Override
    public void handle(SaveRealTimeSensorDataCommand command) {

        List<RealTimeSensorData> realTimeSensorDataList = new ArrayList<>();

        for(RealTimeSensorDataDto dto : command.realTimeSensorDataList()){
            realTimeSensorDataList.add(new RealTimeSensorData(dto));
        }

        realTimeSensorDataRepository.saveAll(realTimeSensorDataList);

    }
}
