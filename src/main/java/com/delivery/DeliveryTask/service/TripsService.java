package com.delivery.DeliveryTask.service;

import com.delivery.DeliveryTask.model.DeliveryTrip;
import com.delivery.DeliveryTask.service.dto.DeliveryTripDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TripsService {
    //ADMIN
    @Transactional
    DeliveryTripDTO createDeliveryTrip(DeliveryTripDTO deliveryTrip);

    //DELIVERYMAN
    List<DeliveryTripDTO> getAllAssignedTrips(String id);

    //DELIVERYMAN
    DeliveryTripDTO acceptTrip(String tripId);

    //DELIVERYMAN
    DeliveryTripDTO startTrip(String tripId);

    //DELIVERYMAN
    DeliveryTripDTO markTripAsEnded(String tripId);
}
