package com.delivery.DeliveryTask.model;

import com.delivery.DeliveryTask.enums.DeliveryTripStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "delivery_trip")
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTrip {

    @Id
    private ObjectId id;
    private String description;
    private int weight;
    private DeliveryTripStatus status;
    private List<PackageOrder> packages;
    private ObjectId deliveryManId;

}
