package com.techcompany.fastporte.shared.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Notification;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.NotificationType;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.NotificationRepository;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Client;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.User;
import com.techcompany.fastporte.users.domain.model.aggregates.enums.RoleName;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.ClientRepository;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.DriverRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SendNotificationWS {

    private final DriverRepository driverRepository;
    private final ClientRepository clientRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;

    public SendNotificationWS(DriverRepository driverRepository, ClientRepository clientRepository, NotificationRepository notificationRepository, NotificationWebSocketHandler webSocketHandler, ObjectMapper objectMapper) {
        this.driverRepository = driverRepository;
        this.clientRepository = clientRepository;
        this.notificationRepository = notificationRepository;
        this.webSocketHandler = webSocketHandler;
        this.objectMapper = objectMapper;
    }

    public void notifyDriverAndClient(Long driverId, Long clientId, NotificationType notificationType, Long referenceId) {

        List<User> users = new ArrayList<>();

        Optional<Driver> dUser = driverRepository.findById(driverId);
        Optional<Client> cUser = clientRepository.findById(clientId);

        if (dUser.isEmpty() || cUser.isEmpty()) {
            throw new RuntimeException("Error: The driver with id '" + driverId + "' or client with id'" + clientId + "' does not exist in the database");
        }

        users.add(dUser.get().getUser());
        users.add(cUser.get().getUser());

        for (User user : users) {
            SendNotification(user, notificationType, referenceId);
        }
    }

    public void NotifyRangeOfUsers(List<User> users, NotificationType notificationType, Long referenceId) {
        for (User user : users) {
            SendNotification(user, notificationType, referenceId);
        }
    }

    public void SendNotification(User user, NotificationType notificationType, Long referenceId) {
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
