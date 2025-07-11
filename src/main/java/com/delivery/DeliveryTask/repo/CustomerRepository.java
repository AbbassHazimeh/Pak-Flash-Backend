package com.delivery.DeliveryTask.repo;

import com.delivery.DeliveryTask.model.Customer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<Customer,String> {

    Customer findById(ObjectId customerId);
}
