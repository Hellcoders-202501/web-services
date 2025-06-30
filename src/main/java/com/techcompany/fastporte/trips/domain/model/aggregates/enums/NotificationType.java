package com.techcompany.fastporte.trips.domain.model.aggregates.enums;

public enum NotificationType {
    REQUEST_PUBLISHED,              /// Cuando se publica una solicitud
    REQUEST_APPLIED,                /// Cuando un conductor aplica a una solicitud
    APPLICATION_DELETED,            /// Cuando un cliente elimina la solicitud de un conductor. Se referecia el request asociado

    TRIP_STARTED,                   /// Cuando el viaje inicia
    TRIP_FINISHED_BY_DRIVER,        /// Cuando el viaje finaliza por el conductor
    TRIP_FINISHED_BY_CLIENT,        /// Cuando el viaje finaliza por el cliente
    TRIP_COMPLETED,                 /// Cuando el viaje se completa

    TRIP_PAID,                      /// Cuando el viaje se pagó al conductor

    CONTRACT_CREATED                /// Cuando se crea un contrato
}