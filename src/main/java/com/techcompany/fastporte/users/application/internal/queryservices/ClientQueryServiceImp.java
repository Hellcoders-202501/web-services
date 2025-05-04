package com.techcompany.fastporte.users.application.internal.queryservices;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Client;
import com.techcompany.fastporte.users.domain.model.queries.client.CheckClientExistsByIdQuery;
import com.techcompany.fastporte.users.domain.model.queries.client.GetAllClientsQuery;
import com.techcompany.fastporte.users.domain.model.queries.client.GetClientByIdQuery;
import com.techcompany.fastporte.users.domain.services.client.ClientQueryService;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ClientQueryServiceImp implements ClientQueryService {

    private final ClientRepository clientRepository;

    public ClientQueryServiceImp(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<Client> handle(GetClientByIdQuery query) {
        return clientRepository.findById(query.id());
    }

    @Override
    public List<Client> handle(GetAllClientsQuery query) {
        return clientRepository.findAll();
    }

    @Override
    public Boolean handle(CheckClientExistsByIdQuery query) {
        return clientRepository.existsById(query.clientId());
    }
}
