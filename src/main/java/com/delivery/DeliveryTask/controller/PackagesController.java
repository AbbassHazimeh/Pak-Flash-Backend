package com.delivery.DeliveryTask.controller;

import com.delivery.DeliveryTask.model.PackageOrder;
import com.delivery.DeliveryTask.service.PackagesService;
import lombok.RequiredArgsConstructor;
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
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PackageOrder> createPackage(@RequestBody PackageOrder newPackage){
        PackageOrder createdPackage = packagesService.createPackage(newPackage);
        return new ResponseEntity<>(createdPackage, HttpStatus.CREATED);
    }

    //ADMIN
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PackageOrder>> viewAllPackages(){
        return new ResponseEntity<>(packagesService.getAllPackages(),HttpStatus.OK);
    }

    //ADMIN
    @GetMapping("/assigned")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PackageOrder>> viewAllAssignedPackages(){
        return new ResponseEntity<>(packagesService.getAllAssignedPackages(),HttpStatus.OK);
    }

    //CUSTOMER
    @GetMapping("/assignedByCustomer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<PackageOrder>> viewAllAssignedPackagesByCustomer(@RequestParam String customerId){
        return new ResponseEntity<>(packagesService.getAllAssignedPackagesByCustomer(customerId),HttpStatus.OK);
    }
    //DELIVERYMAN
    @PutMapping("{packageId}/delivered")
    @PreAuthorize("hasRole('DELIVERYMAN')")
    public ResponseEntity<PackageOrder> deliverPackage(@PathVariable String packageId){
        return new ResponseEntity<>(packagesService.markPackageAsDelivered(packageId),HttpStatus.OK);
    }

    //CUSTOMER
    @PutMapping("{packageId}/confirm")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PackageOrder> ConfirmPackage(@PathVariable String packageId){
        return new ResponseEntity<>(packagesService.markPackageAsConfirmed(packageId),HttpStatus.OK);
    }
}
