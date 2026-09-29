package com.example.capstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name is required")
    @Size(min = 5, max = 20, message = "Name length must be between 5 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = " Name must contain only characters ")
    @Column(columnDefinition = "varchar(20) not null")
    private String name;

    @Email
    @NotEmpty(message = "Email is required")
    @Size(max = 50, message = "Email must not exceed 50 characters")
    @Column(columnDefinition = " varchar(50) not null unique")
    private String email;

    @Column(columnDefinition = "varchar(15) not null unique")
    @Pattern(
            regexp = "^\\+9665[0-9]{8}$",
            message = "Invalid Saudi mobile number. Must match format: +9665XXXXXXXX"
    )
    @NotBlank(message = "Phone is required")
    private String phone;


    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "Password must contain at least one letter and one number"
    )
    @Column(columnDefinition = "varchar(255) not null")
    private String password;
}
