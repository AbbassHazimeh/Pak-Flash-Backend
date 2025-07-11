package com.delivery.DeliveryTask.controller;

import com.delivery.DeliveryTask.model.Customer;
import com.delivery.DeliveryTask.model.DeliveryMan;
import com.delivery.DeliveryTask.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    //ADMIN
    @PostMapping("/customer")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer newcustomer){
        Customer createdCustomer = usersService.createCustomer(newcustomer);
        return new ResponseEntity<>(createdCustomer,HttpStatus.CREATED);

    }

    //ADMIN
    @PostMapping("/deliveryman")
    public ResponseEntity<DeliveryMan> createDeliveryMan(@RequestBody DeliveryMan newDeliveryMan){
        DeliveryMan createdDeliveryMan = usersService.createDeliveryMan(newDeliveryMan);
        return new ResponseEntity<>(createdDeliveryMan,HttpStatus.CREATED);
    }

    //NOBODY we can put it for the admin
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> viewAllCustomers(){
        return new ResponseEntity<>(usersService.getAllCustomers(),HttpStatus.OK);
    }

    //NOBODY we can put it for the admin
    @GetMapping("/deliverymen")
    public ResponseEntity<List<DeliveryMan>> viewAllDeliveryMen(){
        return new ResponseEntity<>(usersService.getAllDeliveryMen(),HttpStatus.OK);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id){
        return usersService.getCustomerById(id)
                .map(customer -> new ResponseEntity<>(customer, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @GetMapping("/deliveryman/{id}")
    public ResponseEntity<DeliveryMan> getDeliveryManById(@PathVariable String id){
        return usersService.getDeliveryManById(id)
                .map(deliveryMan-> new ResponseEntity<>(deliveryMan, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
