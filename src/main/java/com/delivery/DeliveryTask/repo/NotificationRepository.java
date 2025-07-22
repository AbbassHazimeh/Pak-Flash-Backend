package com.delivery.DeliveryTask.repo;

import com.delivery.DeliveryTask.model.Notifications;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notifications,String> {
}
