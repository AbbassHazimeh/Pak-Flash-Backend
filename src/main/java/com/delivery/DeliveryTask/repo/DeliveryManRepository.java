package com.delivery.DeliveryTask.repo;

import com.delivery.DeliveryTask.model.DeliveryMan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryManRepository extends JpaRepository<DeliveryMan,String> {

}
