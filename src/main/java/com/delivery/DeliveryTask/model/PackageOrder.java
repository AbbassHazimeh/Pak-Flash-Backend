package com.delivery.DeliveryTask.model;

import com.delivery.DeliveryTask.enums.PackageStatus;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "package_orders")
@NoArgsConstructor
@AllArgsConstructor
public class PackageOrder {
    @Id
    private ObjectId id;
    private String name;
    private String phone;
    private PackageStatus status;
    private ObjectId customerId;
    private ObjectId delivery_trip_id;
}
