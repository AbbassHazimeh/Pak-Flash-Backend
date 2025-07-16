package com.delivery.DeliveryTask.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @NotNull
    private String name;
    @NotNull
    private String phone;
    @NotNull
    private String address;

}
