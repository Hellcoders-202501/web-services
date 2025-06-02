package com.techcompany.fastporte.trips.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.shared.resources.summary.DriverSummaryResource;
import com.techcompany.fastporte.shared.transform.DriverSummaryResourceFromEntityAssembler;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Contract;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Payment;
import com.techcompany.fastporte.trips.interfaces.rest.resources.ContractResource;
import com.techcompany.fastporte.trips.interfaces.rest.resources.PaymentResource;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.User;

public class ContractResourceFromEntityAssembler {

    public static ContractResource toResourceFromEntity(Contract contract) {

        Driver driver = contract.getDriver();
        User driverUser = driver.getUser();
        Payment payment = contract.getPayment();

        return new ContractResource(
                contract.getId(),
                DriverSummaryResourceFromEntityAssembler.assemble(driver),
                new PaymentResource(payment.getId(), payment.getAmount(), payment.getStatus().getStatus().name())
        );
    }
}
