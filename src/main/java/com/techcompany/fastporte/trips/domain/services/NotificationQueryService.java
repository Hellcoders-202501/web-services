package com.techcompany.fastporte.trips.domain.services;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Notification;
import com.techcompany.fastporte.trips.domain.model.queries.GetAllNotificationsByUserIdQuery;
import com.techcompany.fastporte.trips.domain.model.queries.GetUnreadNotificationsByUserIdQuery;

import java.util.List;

public interface NotificationQueryService {
    List<Notification> handle(GetAllNotificationsByUserIdQuery query);
    List<Notification> handle(GetUnreadNotificationsByUserIdQuery query);
}
