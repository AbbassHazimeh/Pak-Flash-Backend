package com.delivery.DeliveryTask.repo;

import com.delivery.DeliveryTask.model.UserClass;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserClassRepository extends MongoRepository<UserClass,String> {
}
