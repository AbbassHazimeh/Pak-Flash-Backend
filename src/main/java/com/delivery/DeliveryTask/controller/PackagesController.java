package com.delivery.DeliveryTask.controller;

import com.delivery.DeliveryTask.service.PackagesService;
import com.delivery.DeliveryTask.service.dto.PackageOrderDTO;
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
    public ResponseEntity<PackageOrderDTO> createPackage(@RequestBody PackageOrderDTO newPackage){
        PackageOrderDTO createdPackage = packagesService.createPackage(newPackage);
        return new ResponseEntity<>(createdPackage, HttpStatus.CREATED);
    }

    //ADMIN
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PackageOrderDTO>> viewAllPackages(){
        return new ResponseEntity<>(packagesService.getAllPackages(),HttpStatus.OK);
    }

    //ADMIN
    @GetMapping("/assigned")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PackageOrderDTO>> viewAllAssignedPackages(){
        return new ResponseEntity<>(packagesService.getAllAssignedPackages(),HttpStatus.OK);
    }

    //CUSTOMER
    @GetMapping("{customerId}/assignedByCustomer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<PackageOrderDTO>> viewAllAssignedPackagesByCustomer(@PathVariable String customerId){
        return new ResponseEntity<>(packagesService.getAllAssignedPackagesByCustomer(customerId),HttpStatus.OK);
    }
    //DELIVERYMAN
    @GetMapping("{deliveryManId}/assignedByDeliveryMan")
    @PreAuthorize("hasRole('DELIVERYMAN')")
    public ResponseEntity<List<PackageOrderDTO>> viewAllAssignedPackagesByDeliveryMan(@PathVariable String deliveryManId){
        return new ResponseEntity<>(packagesService.getAllAssignedPackagesByDeliveryMan(deliveryManId),HttpStatus.OK);
    }
    //DELIVERYMAN
    @PutMapping("{packageId}/delivered")
    @PreAuthorize("hasRole('DELIVERYMAN')")
    public ResponseEntity<PackageOrderDTO> deliverPackage(@PathVariable String packageId){
        return new ResponseEntity<>(packagesService.markPackageAsDelivered(packageId),HttpStatus.OK);
    }

    //CUSTOMER
    @PutMapping("{packageId}/confirm")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PackageOrderDTO> ConfirmPackage(@PathVariable String packageId){
        return new ResponseEntity<>(packagesService.markPackageAsConfirmed(packageId),HttpStatus.OK);
    }
}
