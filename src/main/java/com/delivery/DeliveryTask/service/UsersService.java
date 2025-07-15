package com.delivery.DeliveryTask.service;

import com.delivery.DeliveryTask.enums.Role;
import com.delivery.DeliveryTask.model.UserClass;
import com.delivery.DeliveryTask.repo.UserClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UserClassRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserClass createUser(UserClass user) {
        validateUserByRole(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    private void validateUserByRole(UserClass user) {
        if (user.getRole() == Role.CUSTOMER) {
            if (user.getCustomer() == null || user.getCustomer().getName() == null || user.getCustomer().getPhone() == null || user.getCustomer().getAddress() == null) {
                throw new IllegalArgumentException("Customer details are incomplete.");
            }
        } else if (user.getRole() == Role.DELIVERYMAN) {
            if (user.getDeliveryMan() == null || user.getDeliveryMan().getName() == null || user.getDeliveryMan().getPhone() == null || user.getDeliveryMan().getStatus() == null || user.getDeliveryMan().getDeliveryTripId() == null) {
                throw new IllegalArgumentException("DeliveryMan details are incomplete.");
            }
        }
    }
    public UserClass createCustomer(UserClass customer) {
        if (customer.getRole() != Role.CUSTOMER) {
            throw new IllegalArgumentException("Role must be CUSTOMER.");
        }
        return createUser(customer);
    }
    public UserClass createDeliveryMan(UserClass deliveryMan) {
        if (deliveryMan.getRole() != Role.DELIVERYMAN) {
            throw new IllegalArgumentException("Role must be DELIVERYMAN.");
        }
        return createUser(deliveryMan);
    }

    public void updateUser(UserClass user) {
        validateUserByRole(user);
        userRepository.save(user);
    }

    public Optional<UserClass> getUserById(String id) {
        return userRepository.findById(id);
    }
    public List<UserClass> getAllCustomers(){
        return userRepository.findByRole(Role.CUSTOMER);
    }

    public List<UserClass> getAllDeliveryMen(){
        return userRepository.findByRole(Role.DELIVERYMAN);
    }

}
