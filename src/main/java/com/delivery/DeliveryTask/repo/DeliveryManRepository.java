package com.delivery.DeliveryTask.repo;

import com.delivery.DeliveryTask.model.DeliveryMan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryManRepository extends MongoRepository<DeliveryMan,String> {

}
