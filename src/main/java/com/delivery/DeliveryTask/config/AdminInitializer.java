package com.delivery.DeliveryTask.config;

import com.delivery.DeliveryTask.enums.Role;
import com.delivery.DeliveryTask.service.UsersService;
import com.delivery.DeliveryTask.service.dto.UserClassDTO;
import com.delivery.DeliveryTask.service.impl.UsersServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UsersServiceImpl usersService, PasswordEncoder passwordEncoder) {
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (usersService.getAllAdmins().isEmpty()) {
            UserClassDTO admin = new UserClassDTO();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("adminpass"));
            admin.setRole(Role.ADMIN);
            usersService.createAdmin(admin);
            System.out.println("Default admin created: admin/adminpass");
        }
    }
}
