package com.delivery.DeliveryTask.model;

import com.delivery.DeliveryTask.enums.DeliveryTripStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "deliveryTrip")
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTrip {

    @Id
    private String id;
    private String description;
    @Positive
    private int weight;
    @NotNull
    private DeliveryTripStatus status;
    @DBRef
    private List<PackageOrder> packages = new ArrayList<>();
    @Positive
    @NotNull
    private String deliveryManId;

}
