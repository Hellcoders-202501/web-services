package com.techcompany.fastporte.trips.domain.model.aggregates.enums;

public enum NotificationType {
    REQUEST_NOTIFICATION,

    //TRIP_NOTIFICATION,
    TRIP_STARTED,
    TRIP_FINISHED_BY_DRIVER,
    TRIP_FINISHED_BY_CLIENT,
    TRIP_COMPLETED,

    PAYMENT_NOTIFICATION,

    // Cuando el viaje está completado
    CONTRACT_NOTIFICATION

}
