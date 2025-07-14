package com.delivery.DeliveryTask.model;

import com.delivery.DeliveryTask.enums.DeliveryManStatus;
import com.delivery.DeliveryTask.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "delivery_man")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryMan {
    @Id
    private ObjectId id;
    private String name;
    private String username;
    private String phone;
    private DeliveryManStatus status;
    private ObjectId delivery_trip_id;
    private Role role;
}
