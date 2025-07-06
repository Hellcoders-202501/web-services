package com.techcompany.fastporte.trips.domain.services;

import com.techcompany.fastporte.trips.domain.model.commands.AcceptDriverApplicationCommand;
import com.techcompany.fastporte.trips.domain.model.commands.CancelContractCommand;

public interface ContractCommandService {
    void handle(AcceptDriverApplicationCommand command);
    void handle(CancelContractCommand command);
}
