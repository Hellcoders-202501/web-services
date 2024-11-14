package com.techcompany.fastporte.trips.interfaces.rest;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Notification;
import com.techcompany.fastporte.trips.domain.services.NotificationQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notification Management", description = "Operations for managing notifications including retrieval")
public class NotificationController {

    private final NotificationQueryService notificationService;

    public NotificationController(NotificationQueryService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/unread/{userId}")
    public List<Notification> getUnreadNotifications(@PathVariable Long userId) {
        return notificationService.getUnreadNotificationsByUserId(userId);
    }

    @GetMapping("/user/{userId}")
    public List<Notification> getNotificationsByUserId(@PathVariable Long userId) {
        return notificationService.getNotificationsByUserId(userId);
    }

}
