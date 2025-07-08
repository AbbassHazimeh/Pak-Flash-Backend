package com.delivery.DeliveryTask.repo;

import com.delivery.DeliveryTask.model.PackageOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<PackageOrder,String> {
}
