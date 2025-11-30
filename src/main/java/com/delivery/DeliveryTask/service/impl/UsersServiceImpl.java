package com.delivery.DeliveryTask.service.impl;

import com.delivery.DeliveryTask.enums.Role;
import com.delivery.DeliveryTask.exception.InvalidRequestException;
import com.delivery.DeliveryTask.model.UserClass;
import com.delivery.DeliveryTask.repo.UserClassRepository;
import com.delivery.DeliveryTask.service.UsersService;
import com.delivery.DeliveryTask.service.dto.UserClassDTO;
import com.delivery.DeliveryTask.service.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UsersServiceImpl implements UsersService {
    private final UserClassRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UsersServiceImpl(UserClassRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }
    @Override
    public UserClassDTO createAdmin(UserClassDTO user) {
        if (user == null) {
            throw new InvalidRequestException("User data must not be null.");
        }
        log.info("Incoming DTO: {}", user);
        UserClass userClass = userMapper.toEntity(user);
        log.info("Incoming DTO: {}", user);
        UserClass savedUser = userRepository.save(userClass);
        return userMapper.toDto(savedUser);
    }
    @Override
    public UserClassDTO createUser(UserClassDTO user) {
        if (user == null) {
            throw new InvalidRequestException("User data must not be null.");
        }
        validateUserByRole(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Incoming DTO: {}", user);
        UserClass userClass = userMapper.toEntity(user);
        log.info("Incoming DTO: {}", user);
        UserClass savedUser = userRepository.save(userClass);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserClassDTO createCustomer(UserClassDTO customer) {
        if (customer == null) {
            throw new InvalidRequestException("Customer data must not be null.");
        }
        if (customer.getRole() != Role.CUSTOMER) {
            throw new InvalidRequestException("Role must be CUSTOMER.");
        }
        return createUser(customer);
    }
    @Override
    public UserClassDTO createDeliveryMan(UserClassDTO deliveryMan) {
        if (deliveryMan == null) {
            throw new InvalidRequestException("DeliveryMan data must not be null.");
        }
        if (deliveryMan.getRole() != Role.DELIVERYMAN) {
            throw new InvalidRequestException("Role must be DELIVERYMAN.");
        }
        return createUser(deliveryMan);
    }

    @Override
    public void updateUser(UserClassDTO user) {
        if (user == null) {
            throw new InvalidRequestException("User data must not be null.");
        }
        validateUserByRole(user);
        UserClass userClass = userMapper.toEntity(user);
        userMapper.toDto(userRepository.save(userClass));
    }

    @Override
    public Optional<UserClassDTO> getUserById(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("User ID must be provided.");
        }
        Optional<UserClass> optionalUser = userRepository.findById(id);
        System.out.println(optionalUser);
        if (optionalUser.isEmpty()) {
            System.err.println("Invalid user ID passed: " + id);
            throw new InvalidRequestException("User not found with ID: " + id);
        }
        UserClassDTO userDTO = userMapper.toDto(optionalUser.get());
        return Optional.of(userDTO);
    }
    @Override
    public List<UserClassDTO> getAllCustomers(){
        return userRepository.findByRole(Role.CUSTOMER);
    }

    @Override
    public List<UserClassDTO> getAllDeliveryMen(){
        return userRepository.findByRole(Role.DELIVERYMAN);
    }

    public void validateUserByRole(UserClassDTO user) {
        if (user.getRole() == Role.CUSTOMER) {
            if (user.getCustomer() == null || user.getCustomer().getName() == null || user.getCustomer().getPhone() == null || user.getCustomer().getAddress() == null) {
                throw new InvalidRequestException("Customer details are incomplete.");
            }
        } else if (user.getRole() == Role.DELIVERYMAN) {
            if (user.getDeliveryMan() == null || user.getDeliveryMan().getName() == null || user.getDeliveryMan().getPhone() == null || user.getDeliveryMan().getStatus() == null || user.getDeliveryMan().getDeliveryTripId() == null) {
                throw new InvalidRequestException("DeliveryMan details are incomplete.");
            }
        } else {
            throw new InvalidRequestException("Invalid user role.");
        }
    }

    @Override
    public List<UserClassDTO> getAllAdmins() {
        return userRepository.findByRole(Role.ADMIN);
    }
}
