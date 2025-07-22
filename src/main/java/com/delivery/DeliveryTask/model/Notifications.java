package com.delivery.DeliveryTask.model;

import com.delivery.DeliveryTask.enums.NotificationsType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "notifications")

public class Notifications {

    @Id
    private String id;
    @NotNull

    private String message;
    @NotNull
    private LocalDateTime date;
    @NotNull
    private NotificationsType type;
    private boolean read = false;

}
