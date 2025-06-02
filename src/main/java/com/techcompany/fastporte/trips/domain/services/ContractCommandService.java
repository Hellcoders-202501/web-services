package com.techcompany.fastporte.trips.domain.services;

import com.techcompany.fastporte.trips.domain.model.commands.AcceptDriverApplicationCommand;

public interface ContractCommandService {
    void handle(AcceptDriverApplicationCommand command);
}
