package com.techcompany.fastporte.trips.interfaces.rest;

import com.techcompany.fastporte.shared.response.ErrorResponse;
import com.techcompany.fastporte.shared.response.SuccessResponse;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Notification;
import com.techcompany.fastporte.trips.domain.model.commands.MarkNotificationsAsSeenByUserIdCommand;
import com.techcompany.fastporte.trips.domain.model.queries.GetAllNotificationsByUserIdQuery;
import com.techcompany.fastporte.trips.domain.services.NotificationCommandService;
import com.techcompany.fastporte.trips.domain.services.NotificationQueryService;
import com.techcompany.fastporte.trips.interfaces.rest.transform.fromEntity.NotificationInformationResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> markNotificationsAsSeenByUserId(@PathVariable Long userId) {
        try {
            notificationCommandService.handle(new MarkNotificationsAsSeenByUserIdCommand(userId));
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Notificaciones marcadas como leídas"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @Operation(summary = "Get notifications by user ID")
    @GetMapping("/{userId}/users")
    public ResponseEntity<?> getNotificationsByUserId(@PathVariable Long userId) {

        try {
            List<Notification> notifications = notificationQueryService.handle(new GetAllNotificationsByUserIdQuery(userId));

            if (notifications.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SuccessResponse("Sin notificaciones"));
            }

            var resourceList =  notifications.stream()
                    .map(NotificationInformationResourceFromEntityAssembler::fromEntity)
                    .toList();

            return ResponseEntity.status(HttpStatus.OK).body(resourceList);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

}
