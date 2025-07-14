package com.delivery.DeliveryTask.service;

import com.delivery.DeliveryTask.enums.Role;
import com.delivery.DeliveryTask.model.Customer;
import com.delivery.DeliveryTask.model.DeliveryMan;
import com.delivery.DeliveryTask.model.UserClass;
import com.delivery.DeliveryTask.repo.CustomerRepository;
import com.delivery.DeliveryTask.repo.DeliveryManRepository;
import com.delivery.DeliveryTask.repo.UserClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final CustomerRepository customerRepository;
    private final DeliveryManRepository deliveryManRepository;
    private final UserClassRepository userRepository;

    public UserClass createUser(UserClass user){
        return userRepository.save(user);
    }
    public Customer createCustomer(Customer customer) {
        UserClass user = userRepository.findById(customer.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() != Role.CUSTOMER) {
            throw new RuntimeException("User role is not CUSTOMER");
        }
        return customerRepository.save(customer);
    }
    public DeliveryMan createDeliveryMan(DeliveryMan deliveryMan) {
        UserClass user = userRepository.findById(deliveryMan.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() != Role.DELIVERYMAN) {
            throw new RuntimeException("User role is not DELIVERYMAN");
        }
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
