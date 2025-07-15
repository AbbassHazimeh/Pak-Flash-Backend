package com.delivery.DeliveryTask.repo;

import com.delivery.DeliveryTask.enums.Role;
import com.delivery.DeliveryTask.model.UserClass;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface UserClassRepository extends MongoRepository<UserClass,String> {
    List<UserClass> findByRole(Role role);
    Optional<UserClass> findByUsername(String username);
}
