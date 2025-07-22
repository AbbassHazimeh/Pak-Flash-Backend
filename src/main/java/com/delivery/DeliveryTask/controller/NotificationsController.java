package com.delivery.DeliveryTask.controller;

import com.delivery.DeliveryTask.model.Notifications;
import com.delivery.DeliveryTask.repo.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.Notification;
import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationsController {


    private final NotificationRepository notificationRepository;
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Notifications> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> markAsRead(@PathVariable String id) {
        Notifications notifications = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + id));

        notifications.setRead(true);
        notificationRepository.save(notifications);

        return ResponseEntity.ok().build();
    }
}
