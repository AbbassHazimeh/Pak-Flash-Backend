package com.delivery.DeliveryTask.controller;


import com.delivery.DeliveryTask.model.Customer;
import com.delivery.DeliveryTask.model.DeliveryMan;
import com.delivery.DeliveryTask.model.DeliveryTrip;
import com.delivery.DeliveryTask.model.PackageOrder;
import com.delivery.DeliveryTask.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @PostMapping("/customers")
    public ResponseEntity<Customer> CreateCustomer(@RequestBody Customer customer){
        Customer createdCustomer = adminService.createCustomer(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }
    @PostMapping("/deliverymen")
    public ResponseEntity<DeliveryMan> CreateDeliveryMan(@RequestBody DeliveryMan deliveryMan){
        DeliveryMan createdDeliveryMan = adminService.createDeliveryMan(deliveryMan);
        return new ResponseEntity<>(createdDeliveryMan, HttpStatus.CREATED);
    }
    @PostMapping("/packages")
    public ResponseEntity<PackageOrder> CreatePackage(@RequestBody PackageOrder newpackage){
        PackageOrder createdPackage = adminService.createPackage(newpackage);
        return new ResponseEntity<>(createdPackage, HttpStatus.CREATED);
    }
    @PostMapping("/trips")
    public ResponseEntity<DeliveryTrip> CreateTrip(@RequestBody DeliveryTrip deliveryTrip){
        DeliveryTrip createdTrip = adminService.createTrip(deliveryTrip);
        return new ResponseEntity<>(createdTrip, HttpStatus.CREATED);
    }

}
