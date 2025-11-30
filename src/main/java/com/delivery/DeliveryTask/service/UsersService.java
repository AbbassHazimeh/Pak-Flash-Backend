package com.delivery.DeliveryTask.service;

import com.delivery.DeliveryTask.enums.Role;
import com.delivery.DeliveryTask.exception.InvalidRequestException;
import com.delivery.DeliveryTask.model.UserClass;
import com.delivery.DeliveryTask.service.dto.UserClassDTO;

import java.util.List;
import java.util.Optional;

public interface UsersService {
    UserClassDTO createUser(UserClassDTO user);

    UserClassDTO createCustomer(UserClassDTO customer);

    UserClassDTO createDeliveryMan(UserClassDTO deliveryMan);

    void updateUser(UserClassDTO user);

    Optional<UserClassDTO> getUserById(String id);

    List<UserClassDTO> getAllCustomers();

    List<UserClassDTO> getAllDeliveryMen();

    List<UserClassDTO> getAllAdmins();
    UserClassDTO createAdmin(UserClassDTO user);
}
