package com.delivery.DeliveryTask.model;

import com.delivery.DeliveryTask.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private String name;
    private String phone;
    private String address;

}
