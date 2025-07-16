package com.delivery.DeliveryTask.model;

import com.delivery.DeliveryTask.enums.DeliveryManStatus;
import com.delivery.DeliveryTask.enums.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryMan {
    @NotNull
    private String name;
    @NotNull
    private String phone;
    @NotNull
    private DeliveryManStatus status;
    @NotNull
    private String deliveryTripId;
}
