package com.techcompany.fastporte.trips.application.internal.queryservices;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Notification;
import com.techcompany.fastporte.trips.domain.model.queries.GetAllNotificationsByUserIdQuery;
import com.techcompany.fastporte.trips.domain.model.queries.GetUnreadNotificationsByUserIdQuery;
import com.techcompany.fastporte.trips.domain.services.NotificationQueryService;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationQueryServiceImp implements NotificationQueryService {

    private final NotificationRepository notificationRepository;

    public NotificationQueryServiceImp(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Notification> handle(GetAllNotificationsByUserIdQuery query) {
        return notificationRepository.findAllByUserId(query.userId());
    }

    @Override
    public List<Notification> handle(GetUnreadNotificationsByUserIdQuery query) {
        return notificationRepository.findAllByUserIdAndSeen(query.userId(), false);
    }
}
