package com.delivery.DeliveryTask.controller;

import com.delivery.DeliveryTask.enums.Role;
import com.delivery.DeliveryTask.model.UserClass;
import com.delivery.DeliveryTask.service.dto.UserClassDTO;
import com.delivery.DeliveryTask.service.impl.UsersServiceImpl;
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

    private final UsersServiceImpl usersService;

    //ADMIN
    @PostMapping("/customer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserClassDTO> createCustomer(@RequestBody UserClassDTO newCustomer){
        if (newCustomer.getRole() != Role.CUSTOMER || newCustomer.getCustomer() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return new ResponseEntity<>(usersService.createCustomer(newCustomer), HttpStatus.CREATED);

    }

    //ADMIN
    @PostMapping("/deliveryman")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserClassDTO> createDeliveryMan(@RequestBody UserClassDTO newDeliveryMan){
        if (newDeliveryMan.getRole() != Role.DELIVERYMAN || newDeliveryMan.getDeliveryMan() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return new ResponseEntity<>(usersService.createDeliveryMan(newDeliveryMan), HttpStatus.CREATED);
    }

    //NOBODY we can put it for the admin
    @GetMapping("/customers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserClassDTO>> viewAllCustomers(){
        return new ResponseEntity<>(usersService.getAllCustomers(), HttpStatus.OK);
    }

    //NOBODY we can put it for the admin
    @GetMapping("/deliverymen")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserClassDTO>> viewAllDeliveryMen(){
        return new ResponseEntity<>(usersService.getAllDeliveryMen(), HttpStatus.OK);
    }

    @GetMapping("/customer/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserClassDTO> getCustomerById(@PathVariable String id){
        return usersService.getUserById(id)
                .map(customer -> new ResponseEntity<>(customer, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @GetMapping("/deliveryman/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserClassDTO> getDeliveryManById(@PathVariable String id){
        return usersService.getUserById(id)
                .map(deliveryMan-> new ResponseEntity<>(deliveryMan, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
