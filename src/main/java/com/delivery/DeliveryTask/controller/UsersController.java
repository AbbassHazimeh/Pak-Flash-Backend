package com.delivery.DeliveryTask.controller;

import com.delivery.DeliveryTask.enums.Role;
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


    @PostMapping //first time only for creation of the admin
    public ResponseEntity<UserClass> createUser(@RequestBody UserClass newUser){
        if (newUser.getRole() != Role.ADMIN) {
            return ResponseEntity.badRequest().body(null);
        }
        return new ResponseEntity<>(usersService.createUser(newUser), HttpStatus.CREATED);
    }

    //ADMIN
    @PostMapping("/customer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserClass> createCustomer(@RequestBody UserClass newCustomer){
        if (newCustomer.getRole() != Role.CUSTOMER || newCustomer.getCustomer() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return new ResponseEntity<>(usersService.createCustomer(newCustomer), HttpStatus.CREATED);

    }

    //ADMIN
    @PostMapping("/deliveryman")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserClass> createDeliveryMan(@RequestBody UserClass newDeliveryMan){
        if (newDeliveryMan.getRole() != Role.DELIVERYMAN || newDeliveryMan.getDeliveryMan() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return new ResponseEntity<>(usersService.createDeliveryMan(newDeliveryMan), HttpStatus.CREATED);
    }

    //NOBODY we can put it for the admin
    @GetMapping("/customers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserClass>> viewAllCustomers(){
        return new ResponseEntity<>(usersService.getAllCustomers(), HttpStatus.OK);
    }

    //NOBODY we can put it for the admin
    @GetMapping("/deliverymen")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserClass>> viewAllDeliveryMen(){
        return new ResponseEntity<>(usersService.getAllDeliveryMen(), HttpStatus.OK);
    }

    @GetMapping("/customer/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserClass> getCustomerById(@PathVariable String id){
        return usersService.getUserById(id)
                .map(customer -> new ResponseEntity<>(customer, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @GetMapping("/deliveryman/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserClass> getDeliveryManById(@PathVariable String id){
        return usersService.getUserById(id)
                .map(deliveryMan-> new ResponseEntity<>(deliveryMan, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
