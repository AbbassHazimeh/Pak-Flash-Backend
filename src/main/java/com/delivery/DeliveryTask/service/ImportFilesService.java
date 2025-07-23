package com.delivery.DeliveryTask.service;

import com.delivery.DeliveryTask.enums.DeliveryManStatus;
import com.delivery.DeliveryTask.enums.Role;
import com.delivery.DeliveryTask.exception.InvalidRequestException;
import com.delivery.DeliveryTask.model.Customer;
import com.delivery.DeliveryTask.model.DeliveryMan;
import com.delivery.DeliveryTask.model.UserClass;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
public class ImportFilesService {

    private final UsersService usersService;
    private final Executor executor;

    public void importUsersFromFile(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineCount = 0;

            while ((line = reader.readLine()) != null) {
                if (lineCount++ == 0) continue;

                final String currentLine = line;

                executor.execute(() -> {
                    try {
                        UserClass user = parseUserFromCsv(currentLine);
                        System.out.println("Parsed user with role: " + user.getRole() + " username: " + user.getUsername());
                        usersService.createUser(user);
                        System.out.println("Successfully saved user: " + user.getUsername());
                    } catch (Exception e) {
                        System.err.println("Error processing line: " + currentLine);
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            throw new InvalidRequestException("Failed to read CSV file");
        }
    }

    private UserClass parseUserFromCsv(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 8) {
            throw new InvalidRequestException("CSV line format is invalid");
        }
        String username = parts[0];
        String password = parts[1];
        String roleString = parts[2].toUpperCase();
        System.out.println("Role string: '" + roleString + "' (length: " + roleString.length() + ")");
        String name = parts[3];
        String phone = parts[4];
        String address = parts[5];
        String statusString = parts[6];
        String deliveryTripId = parts[7];

        Role role = Role.valueOf(roleString);

        UserClass user = new UserClass();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);

        if (role == Role.CUSTOMER) {
            Customer customer = new Customer();
            customer.setName(name);
            customer.setPhone(phone);
            customer.setAddress(address);
            user.setCustomer(customer);
        } else if (role == Role.DELIVERYMAN) {
            DeliveryMan deliveryMan = new DeliveryMan();
            deliveryMan.setName(name);
            deliveryMan.setPhone(phone);
            deliveryMan.setStatus(DeliveryManStatus.valueOf(statusString.toUpperCase()));
            deliveryMan.setDeliveryTripId(deliveryTripId);
            user.setDeliveryMan(deliveryMan);
        } else {
            throw new InvalidRequestException("Unsupported role: " + role);
        }
        return user;
    }
}
