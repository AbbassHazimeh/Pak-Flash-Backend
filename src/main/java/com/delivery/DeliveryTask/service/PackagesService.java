package com.delivery.DeliveryTask.service;

import com.delivery.DeliveryTask.enums.PackageStatus;
import com.delivery.DeliveryTask.enums.Role;
import com.delivery.DeliveryTask.model.Customer;
import com.delivery.DeliveryTask.model.PackageOrder;
import com.delivery.DeliveryTask.model.UserClass;
import com.delivery.DeliveryTask.repo.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PackagesService {

    private final PackageRepository packageRepository;
    private final UsersService usersService;

    public PackageOrder createPackage(PackageOrder newPackage) {
        newPackage.setStatus(PackageStatus.PENDING);
        String customerId = String.valueOf(newPackage.getCustomerId());
        UserClass user = usersService.getUserById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if(user.getRole() != Role.CUSTOMER){
            throw new IllegalArgumentException("not Customer");
        }
        Customer customer = user.getCustomer();
        if(customer == null){
            throw new IllegalArgumentException("Customer id is not founded");
        }

        newPackage.setPhone(customer.getPhone());
        return packageRepository.save(newPackage);
    }
    public void savePackage(PackageOrder packageOrder) {
        packageRepository.save(packageOrder);
    }

    public List<PackageOrder> getAllPackages() {
        return packageRepository.findAll();
    }
    public List<PackageOrder> getAllAssignedPackages() {
        List<PackageOrder> assignedPackages = packageRepository.findByStatusIn(List.of(PackageStatus.ASSIGNED));
        if (assignedPackages.isEmpty()) {
            throw new RuntimeException("No assigned packages");
        }
        return assignedPackages;
    }
    public List<PackageOrder> getAllAssignedPackagesByCustomer(String customerId) {
        List<PackageOrder> assignedPackages = packageRepository.findByCustomerIdAndStatusIn(customerId, List.of(PackageStatus.ASSIGNED));
        if (assignedPackages.isEmpty()) {
            throw new RuntimeException("No assigned packages");
        }
        return assignedPackages;
    }
    public PackageOrder markPackageAsDelivered(String id){
        Optional<PackageOrder> deliveredPackage = packageRepository.findById(id);
        return deliveredPackage.map(packageOrder -> {
            if(packageOrder.getStatus() == PackageStatus.ASSIGNED){// the package must be assigned to him so the admin must assign before it and then delivery can deliver it
                packageOrder.setStatus(PackageStatus.DELIVERED);
                return packageRepository.save(packageOrder);
            }else{
                throw new IllegalStateException("Package is not assigned how did you deliver it man !");
            }
        }).orElse(null);
    }
    public PackageOrder markPackageAsConfirmed(String id){
        Optional<PackageOrder> confirmedPackage = packageRepository.findById(id);
        return confirmedPackage.map(packageOrder -> {
            if(packageOrder.getStatus() == PackageStatus.DELIVERED){// the package must be Delivered to the customer to confirm it
                packageOrder.setStatus(PackageStatus.CONFIRMED);
                return packageRepository.save(packageOrder);
            }else{
                throw new IllegalStateException("Package is not assigned it can not be confirmed before assignment !");
            }
        }).orElse(null);
    }

    public PackageOrder findPackageById(String Id) {
        return (packageRepository.findPackageById(Id));
    }
}
