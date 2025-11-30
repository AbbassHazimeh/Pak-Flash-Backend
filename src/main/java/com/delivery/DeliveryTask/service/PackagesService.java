package com.delivery.DeliveryTask.service;

import com.delivery.DeliveryTask.model.PackageOrder;
import com.delivery.DeliveryTask.service.dto.PackageOrderDTO;

import java.util.List;

public interface PackagesService {
    PackageOrderDTO createPackage(PackageOrderDTO newPackage);

    void savePackageFromTrip(PackageOrder packageOrder);

    List<PackageOrderDTO> getAllPackages();

    List<PackageOrderDTO> getAllAssignedPackages();

    List<PackageOrderDTO> getAllAssignedPackagesByCustomer(String customerId);
    List<PackageOrderDTO> getAllAssignedPackagesByDeliveryMan(String deliveryManId);

    PackageOrderDTO markPackageAsDelivered(String id);

    PackageOrderDTO markPackageAsConfirmed(String id);

    PackageOrderDTO findPackageById(String id);
}
