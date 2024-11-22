package com.techcompany.fastporte.trips.interfaces.rest;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Notification;
import com.techcompany.fastporte.trips.domain.model.commands.MarkNotificationsAsSeenByUserIdCommand;
import com.techcompany.fastporte.trips.domain.model.queries.GetAllNotificationsByUserIdQuery;
import com.techcompany.fastporte.trips.domain.services.NotificationCommandService;
import com.techcompany.fastporte.trips.domain.services.NotificationQueryService;
import com.techcompany.fastporte.trips.interfaces.rest.resources.NotificationInformationResource;
import com.techcompany.fastporte.trips.interfaces.rest.transform.fromEntity.NotificationInformationResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notification Management", description = "Operations for managing notifications including retrieval and marking as seen")
public class NotificationController {

    private final NotificationQueryService notificationQueryService;
    private final NotificationCommandService notificationCommandService;

    public NotificationController(NotificationQueryService notificationQueryService, NotificationCommandService notificationCommandService) {
        this.notificationQueryService = notificationQueryService;
        this.notificationCommandService = notificationCommandService;
    }

    @Operation(summary = "Mark notifications as seen by user ID")
    @PostMapping("/{userId}/read")
    public void markNotificationsAsSeenByUserId(@PathVariable Long userId) {
        notificationCommandService.handle(new MarkNotificationsAsSeenByUserIdCommand(userId));
    }

    @Operation(summary = "Get notifications by user ID")
    @GetMapping("/{userId}/users")
    public List<NotificationInformationResource> getNotificationsByUserId(@PathVariable Long userId) {

        List<Notification> notifications = notificationQueryService.handle(new GetAllNotificationsByUserIdQuery(userId));
        return notifications.stream()
            .map(NotificationInformationResourceFromEntityAssembler::fromEntity)
            .toList();
    }

}
