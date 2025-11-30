package com.delivery.DeliveryTask.service.dto;

import com.delivery.DeliveryTask.enums.PackageStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PackageOrderDTO {
    private String id;
    private String name;
    private String phone;
    private PackageStatus status;
    private String customerId;
    private String deliveryTripId;
    private LocalDateTime time;
}
