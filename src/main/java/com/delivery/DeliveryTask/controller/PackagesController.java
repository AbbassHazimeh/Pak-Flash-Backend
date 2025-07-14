package com.delivery.DeliveryTask.controller;

import com.delivery.DeliveryTask.model.Customer;
import com.delivery.DeliveryTask.model.PackageOrder;
import com.delivery.DeliveryTask.service.PackagesService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/packages")
@RequiredArgsConstructor
public class PackagesController {
    @Autowired
    private PackagesService packagesService;

    //ADMIN
    @PostMapping("admin/package")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PackageOrder> createPackage(@RequestBody PackageOrder newPackage){
        PackageOrder createdPackage = packagesService.createPackage(newPackage);
        return new ResponseEntity<>(createdPackage, HttpStatus.CREATED);
    }

    //ADMIN
    @GetMapping("admin/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PackageOrder>> viewAllPackages(){
        return new ResponseEntity<>(packagesService.getAllPackages(),HttpStatus.OK);
    }

    //ADMIN
    @GetMapping("admin/assigned")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PackageOrder>> viewAllAssignedPackages(){
        return new ResponseEntity<>(packagesService.getAllAssignedPackages(),HttpStatus.OK);
    }

    //CUSTOMER
    @GetMapping("/customer/{customerId}/assigned")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<PackageOrder>> viewAllAssignedPackagesByCustomer(@PathVariable ObjectId customerId){
        return new ResponseEntity<>(packagesService.getAllAssignedPackagesByCustomer(customerId),HttpStatus.OK);
    }
    //DELIVERYMAN
    @PutMapping("deliveryMan/{packageId}/delivered")
    @PreAuthorize("hasRole('DELIVERYMAN')")
    public ResponseEntity<PackageOrder> deliverPackage(@PathVariable ObjectId packageId){
        return new ResponseEntity<>(packagesService.markPackageAsDelivered(packageId),HttpStatus.OK);
    }

    //CUSTOMER
    @PutMapping("customer/{packageId}/confirm")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PackageOrder> ConfirmPackage(@PathVariable ObjectId packageId){
        return new ResponseEntity<>(packagesService.markPackageAsConfirmed(packageId),HttpStatus.OK);
    }
}
