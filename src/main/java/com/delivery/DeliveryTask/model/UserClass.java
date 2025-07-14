package com.delivery.DeliveryTask.model;

import com.delivery.DeliveryTask.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UserClass {
    @Id
    private String username;
    private String password;
    private Role role;

}
