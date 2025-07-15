package com.delivery.DeliveryTask.model;

import com.delivery.DeliveryTask.enums.PackageStatus;

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
    private String name;
    private String phone;
    private PackageStatus status;
    private String customerId;
    private String deliveryTripId;
}
