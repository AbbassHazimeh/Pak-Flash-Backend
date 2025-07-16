package com.delivery.DeliveryTask.model;

import com.delivery.DeliveryTask.enums.PackageStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "packageOrders")
@NoArgsConstructor
@AllArgsConstructor
public class PackageOrder {
    @Id
    private String id;
    @NotNull
    private String name;
    @NotNull
    private String phone;
    @NotNull
    private PackageStatus status;
    @NotNull
    @Positive
    private String customerId;
    @NotNull
    @Positive
    private String deliveryTripId;
}
