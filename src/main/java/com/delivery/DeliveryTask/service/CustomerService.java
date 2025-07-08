package com.delivery.DeliveryTask.service;

import com.delivery.DeliveryTask.model.Customer;
import com.delivery.DeliveryTask.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}
