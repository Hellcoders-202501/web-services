package com.techcompany.fastporte.shared.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Notification;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.NotificationType;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.NotificationRepository;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.User;
import com.techcompany.fastporte.users.domain.model.aggregates.enums.RoleName;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SendNotificationWS {

    private final NotificationRepository notificationRepository;
    private final NotificationWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;

    public SendNotificationWS(NotificationRepository notificationRepository, NotificationWebSocketHandler webSocketHandler, ObjectMapper objectMapper) {
        this.notificationRepository = notificationRepository;
        this.webSocketHandler = webSocketHandler;
        this.objectMapper = objectMapper;
    }

    public void send(User user, NotificationType notificationType, Long referenceId) {

        // Notify
        Notification notification = Notification.builder()
                .createdAt(LocalDateTime.now().withNano(0))
                .type(notificationType)
                .referenceId(referenceId)
                .seen(false)
                .user(user)
                .build();

        notificationRepository.save(notification);

        Long userId = user.getId();
        RoleName roleClient = user.getRoles().stream().findFirst().get().getRoleName();

        if (webSocketHandler.isUserConnected(userId, roleClient)) {
            try {
                String notificationJson = objectMapper.writeValueAsString(notification.toDto());
                webSocketHandler.sendNotification(notificationJson, userId);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: Something went wrong while sending notification. Exception: " + e.getMessage());
            }
        }
    }
}
