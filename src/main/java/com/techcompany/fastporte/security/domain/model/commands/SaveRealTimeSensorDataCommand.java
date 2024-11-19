package com.techcompany.fastporte.security.domain.model.commands;

import com.techcompany.fastporte.shared.dtos.RealTimeSensorDataDto;

import java.util.List;

public record SaveRealTimeSensorDataCommand(
        List<RealTimeSensorDataDto> realTimeSensorDataList
) {
}
