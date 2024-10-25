package com.techcompany.fastporte.trips.infrastructure.acl;

import com.techcompany.fastporte.shared.utils.EnvironmentConstants;
import com.techcompany.fastporte.shared.dtos.DriverInformationDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Component
public class DriverAcl {

    private final RestTemplate restTemplate;
    private final String BASE_URL = EnvironmentConstants.CURRENT_ENV_URL + "/api/drivers/";

    public DriverAcl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<DriverInformationDto> findDriverById(Long id) {
        String url = BASE_URL + id;
        try {
            DriverInformationDto driverDto = restTemplate.getForObject(url, DriverInformationDto.class);
            if (driverDto != null) {
                return Optional.of(driverDto);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    public List<DriverInformationDto> findAllDriversByIdIn(List<Long> ids){
        String url = BASE_URL + "batch";
        ResponseEntity<List<DriverInformationDto>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(ids),
                new ParameterizedTypeReference<List<DriverInformationDto>>() {}
        );
        return response.getBody();
    }
}
