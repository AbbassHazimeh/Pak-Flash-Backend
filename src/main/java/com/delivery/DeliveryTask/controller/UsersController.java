package com.delivery.DeliveryTask.controller;

import com.delivery.DeliveryTask.model.Customer;
import com.delivery.DeliveryTask.model.DeliveryMan;
import com.delivery.DeliveryTask.model.UserClass;
import com.delivery.DeliveryTask.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;


    @PostMapping("/admin/createUser")
    public ResponseEntity<UserClass> createUser(@RequestBody UserClass user){
        return new ResponseEntity<>(usersService.createUser(user), HttpStatus.CREATED);
    }


    //ADMIN
    @PostMapping("/admin/customer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer newcustomer){
        Customer createdCustomer = usersService.createCustomer(newcustomer);
        return new ResponseEntity<>(createdCustomer,HttpStatus.CREATED);

    }

    //ADMIN
    @PostMapping("/admin/deliveryman")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeliveryMan> createDeliveryMan(@RequestBody DeliveryMan newDeliveryMan){
        DeliveryMan createdDeliveryMan = usersService.createDeliveryMan(newDeliveryMan);
        return new ResponseEntity<>(createdDeliveryMan,HttpStatus.CREATED);
    }

    //NOBODY we can put it for the admin
    @GetMapping("/admin/customers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Customer>> viewAllCustomers(){
        return new ResponseEntity<>(usersService.getAllCustomers(),HttpStatus.OK);
    }

    //NOBODY we can put it for the admin
    @GetMapping("/admin/deliverymen")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DeliveryMan>> viewAllDeliveryMen(){
        return new ResponseEntity<>(usersService.getAllDeliveryMen(),HttpStatus.OK);
    }

    @GetMapping("/admin/customer/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id){
        return usersService.getCustomerById(id)
                .map(customer -> new ResponseEntity<>(customer, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @GetMapping("/admin/deliveryman/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeliveryMan> getDeliveryManById(@PathVariable String id){
        return usersService.getDeliveryManById(id)
                .map(deliveryMan-> new ResponseEntity<>(deliveryMan, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
