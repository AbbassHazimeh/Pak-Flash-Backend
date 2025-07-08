package com.delivery.DeliveryTask.model;

import com.delivery.DeliveryTask.enums.DeliveryTripStatus;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class DeliveryTrip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String Id;
    private int Customer_ID;
    private String Description;
    private int Weight;
    private DeliveryTripStatus Status;
    private int Delivery_Trip_Id;
}
