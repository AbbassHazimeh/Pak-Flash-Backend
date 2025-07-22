package com.delivery.DeliveryTask.service;

import com.delivery.DeliveryTask.enums.PackageStatus;
import com.delivery.DeliveryTask.enums.Role;
import com.delivery.DeliveryTask.exception.InvalidRequestException;
import com.delivery.DeliveryTask.exception.PackageNotFoundException;
import com.delivery.DeliveryTask.exception.UserNotFoundException;
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
        if (newPackage == null) {
            throw new InvalidRequestException("Package data must not be null.");
        }
        String customerId = String.valueOf(newPackage.getCustomerId());
        if (customerId.isEmpty() || customerId.isBlank()) {
            throw new InvalidRequestException("Customer ID must be provided.");
        }
        newPackage.setStatus(PackageStatus.PENDING);
        newPackage.setDeliveryTripId(null);
        Optional<UserClass> optionalUser = usersService.getUserById(customerId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Customer not found with ID: " + customerId);
        }

        UserClass user = optionalUser.get();
        if(user.getRole() != Role.CUSTOMER){
            throw new InvalidRequestException("not Customer");
        }
        Customer customer = user.getCustomer();
        if(customer == null){
            throw new InvalidRequestException("Customer details not found.");
        }

        newPackage.setPhone(customer.getPhone());
        return packageRepository.save(newPackage);
    }
    public void savePackage(PackageOrder packageOrder) {
        if (packageOrder == null) {
            throw new InvalidRequestException("PackageOrder must not be null.");
        }
        packageRepository.save(packageOrder);
    }

    public List<PackageOrder> getAllPackages() {
        return packageRepository.findAll();
    }
    public List<PackageOrder> getAllAssignedPackages() {
        List<PackageOrder> assignedPackages = packageRepository.findByStatusIn(List.of(PackageStatus.ASSIGNED));
        if (assignedPackages.isEmpty()) {
            throw new PackageNotFoundException("No assigned packages");
        }
        return assignedPackages;
    }
    public List<PackageOrder> getAllAssignedPackagesByCustomer(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new InvalidRequestException("Customer ID must be provided.");
        }
        List<PackageOrder> assignedPackages = packageRepository.findByCustomerIdAndStatusIn(customerId, List.of(PackageStatus.ASSIGNED));
        if (assignedPackages.isEmpty()) {
            throw new InvalidRequestException("No assigned packages found for customer ID: " + customerId);
        }
        return assignedPackages;
    }
    public PackageOrder markPackageAsDelivered(String id){
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("Package ID must be provided.");
        }
        Optional<PackageOrder> optionalPackage = packageRepository.findById(id);

        if (optionalPackage.isEmpty()) {
            throw new PackageNotFoundException("Package not found with ID: " + id);
        }

        PackageOrder deliveredPackage = optionalPackage.get();
        if (deliveredPackage.getStatus() != PackageStatus.ASSIGNED) {
            throw new InvalidRequestException("Only ASSIGNED packages can be marked as delivered.");
        }
        deliveredPackage.setStatus(PackageStatus.DELIVERED);
        return packageRepository.save(deliveredPackage);
    }
    public PackageOrder markPackageAsConfirmed(String id){
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("Package ID must be provided.");
        }
        Optional<PackageOrder> optionalPackage = packageRepository.findById(id);

        if (optionalPackage.isEmpty()) {
            throw new PackageNotFoundException("Package not found with ID: " + id);
        }

        PackageOrder confirmedPackage = optionalPackage.get();
        if (confirmedPackage.getStatus() != PackageStatus.DELIVERED) {
            throw new InvalidRequestException("Only DELIVERED packages can be confirmed.");
        }
        confirmedPackage.setStatus(PackageStatus.CONFIRMED);
        return packageRepository.save(confirmedPackage);
    }

    public PackageOrder findPackageById(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("Package ID must be provided.");
        }
        PackageOrder founded = packageRepository.findPackageById(id);
        if (founded == null) {
            throw new PackageNotFoundException("Package not found with ID: " + id);
        }
        return founded;
    }
}
