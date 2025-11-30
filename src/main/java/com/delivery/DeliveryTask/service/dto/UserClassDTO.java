package com.delivery.DeliveryTask.service.dto;

import com.delivery.DeliveryTask.enums.Role;
import com.delivery.DeliveryTask.model.Customer;
import com.delivery.DeliveryTask.model.DeliveryMan;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
public class UserClassDTO {
    private String id;
    @NotNull(message = "User name is required")
    @NotBlank(message = "User name cannot be empty")
    @Size(min = 1, max = 255, message = "User name must be between 1 and 255 characters")
    private String username;
    @NotNull
    private Role role;
    private String password;
    private Customer customer;
    private DeliveryMan deliveryMan;
}
