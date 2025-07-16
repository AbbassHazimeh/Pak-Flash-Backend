package com.delivery.DeliveryTask.repo;

import com.delivery.DeliveryTask.enums.PackageStatus;
import com.delivery.DeliveryTask.model.PackageOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends MongoRepository<PackageOrder,String> {
    List<PackageOrder> findByCustomerIdAndStatusIn(String customerId, List<PackageStatus> statusList);
    List<PackageOrder> findByStatusIn(List<PackageStatus> statusList);
    PackageOrder findPackageById(String Id);
}
