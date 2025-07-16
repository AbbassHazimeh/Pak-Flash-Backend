package com.delivery.DeliveryTask.repo;

import com.delivery.DeliveryTask.enums.DeliveryTripStatus;
import com.delivery.DeliveryTask.model.DeliveryTrip;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryTripRepository extends MongoRepository<DeliveryTrip,String> {
    List<DeliveryTrip> findByStatusAndDeliveryManId(DeliveryTripStatus status, String deliveryManId);

}
