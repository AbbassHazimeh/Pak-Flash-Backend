package com.delivery.DeliveryTask.service;

import com.delivery.DeliveryTask.enums.DeliveryManStatus;
import com.delivery.DeliveryTask.enums.Role;
import com.delivery.DeliveryTask.exception.InvalidRequestException;
import com.delivery.DeliveryTask.model.Customer;
import com.delivery.DeliveryTask.model.DeliveryMan;
import com.delivery.DeliveryTask.model.UserClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportFilesService {
    private final UsersService usersService;
    @Qualifier("importTaskExecutor")
    private final Executor executor;
    private static final int CHUNK_SIZE = 20;
    public void importUsersFromFile(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<String> lines = new ArrayList<>();
            String line;
            int lineCount = 0;

            while ((line = reader.readLine()) != null) {
                if (lineCount++ == 0) continue;
                if (lines.size() >= 100) break;
                lines.add(line);
            }

            for (int i = 0; i < lines.size(); i += CHUNK_SIZE) {
                int fromIndex = i;//mn matra7 awal ra2em bkle chunk wsoltello
                int toIndex = Math.min(i + CHUNK_SIZE, lines.size()); // la matra7 ma bade ousal fa aam e5od el minimum ben adesh ana(kl el lines) w adesh bade 7ajem el chunk
                List<String> chunk = lines.subList(fromIndex, toIndex);

                executor.execute(() -> processChunk(chunk));
            }

        } catch (IOException e) {
            throw new InvalidRequestException("Failed to read CSV file");
        }
    }

    private void processChunk(List<String> chunk) {
        for (String line : chunk) {
            try {
                UserClass user = parseUserFromCsv(line);
                usersService.createUser(user);
                log.info("Successfully saved user: {}", user.getUsername());
            } catch (InvalidRequestException e) {
                log.warn("Invalid request: {}. Reason: {}", line, e.getMessage());
            }
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

        Role role;
        try {
            role = Role.valueOf(roleString);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Unsupported role: " + roleString);
        }

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
            try {
                deliveryMan.setStatus(DeliveryManStatus.valueOf(statusString.trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new InvalidRequestException("Invalid delivery man status: " + statusString);
            }
            deliveryMan.setDeliveryTripId(deliveryTripId);
            user.setDeliveryMan(deliveryMan);
        } else {
            throw new InvalidRequestException("Unsupported role: " + role);
        }
        return user;
    }
}
