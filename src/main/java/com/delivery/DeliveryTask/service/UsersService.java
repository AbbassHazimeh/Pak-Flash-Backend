package com.delivery.DeliveryTask.service;

import com.delivery.DeliveryTask.model.Customer;
import com.delivery.DeliveryTask.model.DeliveryMan;
import com.delivery.DeliveryTask.repo.CustomerRepository;
import com.delivery.DeliveryTask.repo.DeliveryManRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final CustomerRepository customerRepository;
    private final DeliveryManRepository deliveryManRepository;

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    public DeliveryMan createDeliveryMan(DeliveryMan deliveryMan) {
        return deliveryManRepository.save(deliveryMan);
    }

    public Optional<Customer> getCustomerById(String id){
        return customerRepository.findById(id);
    }
    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    public List<DeliveryMan> getAllDeliveryMen(){
        return deliveryManRepository.findAll();
    }

    public Optional<DeliveryMan> getDeliveryManById(String id) {
        return deliveryManRepository.findById(id);
    }
}
