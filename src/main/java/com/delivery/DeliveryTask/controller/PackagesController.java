package com.delivery.DeliveryTask.controller;

import com.delivery.DeliveryTask.model.Customer;
import com.delivery.DeliveryTask.model.PackageOrder;
import com.delivery.DeliveryTask.service.PackagesService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/packages")
@RequiredArgsConstructor
public class PackagesController {
    @Autowired
    private PackagesService packagesService;

    //ADMIN
    @PostMapping("/package")
    public ResponseEntity<PackageOrder> createPackage(@RequestBody PackageOrder newPackage){
        PackageOrder createdPackage = packagesService.createPackage(newPackage);
        return new ResponseEntity<>(createdPackage, HttpStatus.CREATED);
    }

    //ADMIN
    @GetMapping("/")
    public ResponseEntity<List<PackageOrder>> viewAllPackages(){
        return new ResponseEntity<>(packagesService.getAllPackages(),HttpStatus.OK);
    }

    //ADMIN
    @GetMapping("/assigned")
    public ResponseEntity<List<PackageOrder>> viewAllAssignedPackages(){
        return new ResponseEntity<>(packagesService.getAllAssignedPackages(),HttpStatus.OK);
    }

    //CUSTOMER
    @GetMapping("/customers/{customerId}/assigned")
    public ResponseEntity<List<PackageOrder>> viewAllAssignedPackagesByCustomer(@PathVariable ObjectId customerId){
        return new ResponseEntity<>(packagesService.getAllAssignedPackagesByCustomer(customerId),HttpStatus.OK);
    }
    //DELIVERYMAN
    @PutMapping("/{packageId}/delivered")
    public ResponseEntity<PackageOrder> deliverPackage(@PathVariable ObjectId packageId){
        return new ResponseEntity<>(packagesService.markPackageAsDelivered(packageId),HttpStatus.OK);
    }

    //CUSTOMER
    @PutMapping("/{packageId}/confirm")
    public ResponseEntity<PackageOrder> ConfirmPackage(@PathVariable ObjectId packageId){
        return new ResponseEntity<>(packagesService.markPackageAsConfirmed(packageId),HttpStatus.OK);
    }
}
