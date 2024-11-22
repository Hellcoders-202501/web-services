package com.techcompany.fastporte.security.interfaces.rest.resources;

import com.techcompany.fastporte.shared.dtos.RealTimeSensorDataDto;

import java.util.List;

public record SaveRealTimeSensorDataResource(
        List<RealTimeSensorDataDto> realTimeSensorDataList
) {
}
