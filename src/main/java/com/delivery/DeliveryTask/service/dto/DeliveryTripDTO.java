package com.delivery.DeliveryTask.service.dto;

import com.delivery.DeliveryTask.enums.DeliveryTripStatus;
import lombok.Data;

import java.util.List;

@Data
public class DeliveryTripDTO {
    private String id;
    private String description;
    private int weight;
    private DeliveryTripStatus status;
    private List<String> packageIds;
    private String deliveryManId;

}
