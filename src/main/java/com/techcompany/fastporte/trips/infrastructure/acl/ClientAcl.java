package com.techcompany.fastporte.trips.infrastructure.acl;

import com.techcompany.fastporte.shared.dtos.ClientInformationDto;
import com.techcompany.fastporte.shared.utils.EnvironmentConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Component
public class ClientAcl {

    @Value("${internal.api.key}")
    String INTERNAL_API_KEY;

    private final RestTemplate restTemplate;
    private final String BASE_URL = EnvironmentConstants.CURRENT_ENV_URL + "/api/v1/clients/";

    public ClientAcl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<ClientInformationDto> findClientById(Long id) {
        String url = BASE_URL + id;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Internal-API-Key", INTERNAL_API_KEY);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ClientInformationDto clientDto = restTemplate.exchange(url, HttpMethod.GET, entity, ClientInformationDto.class).getBody();
            if (clientDto != null) {
                return Optional.of(clientDto);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<ClientInformationDto> findAllClientsByIdIn(List<Long> ids){
        String url = BASE_URL + "batch";
        ResponseEntity<List<ClientInformationDto>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(ids),
                new ParameterizedTypeReference<List<ClientInformationDto>>() {
                }
        );
        return response.getBody();
    }
}
