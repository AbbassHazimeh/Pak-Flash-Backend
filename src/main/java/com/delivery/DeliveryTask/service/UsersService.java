package com.delivery.DeliveryTask.service;

import com.delivery.DeliveryTask.enums.Role;
import com.delivery.DeliveryTask.exception.InvalidRequestException;
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
        if (user == null) {
            throw new InvalidRequestException("User data must not be null.");
        }
        validateUserByRole(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    private void validateUserByRole(UserClass user) {

        if (user.getRole() == Role.CUSTOMER) {
            if (user.getCustomer() == null || user.getCustomer().getName() == null || user.getCustomer().getPhone() == null || user.getCustomer().getAddress() == null) {
                throw new InvalidRequestException("Customer details are incomplete.");
            }
         if (user.getRole() == Role.DELIVERYMAN) {
            if (user.getDeliveryMan() == null || user.getDeliveryMan().getName() == null || user.getDeliveryMan().getPhone() == null || user.getDeliveryMan().getStatus() == null || user.getDeliveryMan().getDeliveryTripId() == null) {
                throw new InvalidRequestException("DeliveryMan details are incomplete.");
            }
         }
        }else {
            throw new InvalidRequestException("Invalid user role.");
        }
    }
    public UserClass createCustomer(UserClass customer) {
        if (customer == null) {
            throw new InvalidRequestException("Customer data must not be null.");
        }
        if (customer.getRole() != Role.CUSTOMER) {
            throw new InvalidRequestException("Role must be CUSTOMER.");
        }
        return createUser(customer);
    }
    public UserClass createDeliveryMan(UserClass deliveryMan) {
        if (deliveryMan == null) {
            throw new InvalidRequestException("DeliveryMan data must not be null.");
        }
        if (deliveryMan.getRole() != Role.DELIVERYMAN) {
            throw new InvalidRequestException("Role must be DELIVERYMAN.");
        }
        return createUser(deliveryMan);
    }

    public void updateUser(UserClass user) {
        if (user == null) {
            throw new InvalidRequestException("User data must not be null.");
        }
        validateUserByRole(user);
        userRepository.save(user);
    }

    public Optional<UserClass> getUserById(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("User ID must be provided.");
        }
        Optional<UserClass> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new InvalidRequestException("User not found with ID: " + id);
        }

        return Optional.of(optionalUser.get());
    }
    public List<UserClass> getAllCustomers(){
        return userRepository.findByRole(Role.CUSTOMER);
    }

    public List<UserClass> getAllDeliveryMen(){
        return userRepository.findByRole(Role.DELIVERYMAN);
    }

}
