package com.techcompany.fastporte.trips.application.internal.commandservices;

import com.techcompany.fastporte.trips.domain.model.commands.MarkNotificationsAsSeenByUserIdCommand;
import com.techcompany.fastporte.trips.domain.services.NotificationCommandService;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationCommandServiceImp implements NotificationCommandService {

    private final NotificationRepository notificationRepository;

    public NotificationCommandServiceImp(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void handle(MarkNotificationsAsSeenByUserIdCommand command) {
        notificationRepository.markAsSeenByUserId(command.userId());
    }
}
