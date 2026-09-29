package com.example.capstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name length must be between 2 and 50 characters")
    @Column(columnDefinition = "varchar(50) not null")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description must not exceed 255 characters")
    @Column(columnDefinition = "varchar(255) not null")
    private String description;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    @Column(columnDefinition = "varchar(255) not null")
    private String address;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City must not exceed 50 characters")
    @Column(columnDefinition = "varchar(50) not null")
    private String city;


    @NotBlank(message = "Phone is required")
    @Pattern(
            regexp = "^\\+966[0-9]{9}$",
            message = "Invalid Saudi phone number. Must match format: +966XXXXXXXXX"
    )
    @Column(columnDefinition = "varchar(13) not null")
    private String phone;

    @NotNull(message = "Opening time is required")
    @Column(columnDefinition = "time not null")
    private LocalTime openingTime;

    @NotNull(message = "Closing time is required")
    @Column(columnDefinition = "time not null")
    private LocalTime closingTime;

    @NotNull(message = "Owner id is required")
    @Column(columnDefinition = "int not null")
    private Integer ownerId;

    @NotNull(message = "Category id is required")
    @Column(columnDefinition = "int not null")
    private Integer categoryId;
}
