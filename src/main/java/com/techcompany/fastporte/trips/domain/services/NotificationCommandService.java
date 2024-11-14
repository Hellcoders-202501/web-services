package com.techcompany.fastporte.trips.domain.services;

import com.techcompany.fastporte.trips.domain.model.commands.MarkNotificationsAsSeenByUserIdCommand;

public interface NotificationCommandService {
    void handle(MarkNotificationsAsSeenByUserIdCommand command);
}
