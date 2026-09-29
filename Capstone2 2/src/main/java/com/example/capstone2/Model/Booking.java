package com.example.capstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "User id is required")
    @Column(columnDefinition = "int not null")
    private Integer userId;

    @NotNull(message = "Place id is required")
    @Column(columnDefinition = "int not null")
    private Integer placeId;

    @NotNull(message = "Booking date is required")
    @Column(columnDefinition = "date not null")
    private LocalDate bookingDate;

    @NotNull(message = "Booking time is required")
    @Column(columnDefinition = "time not null")
    private LocalTime bookingTime;

    @Pattern(
            regexp = "^(PENDING|COMPLETED)$",
            message = "Status must be one of: PENDING,COMPLETED"
    )
    @Column(columnDefinition = "varchar(20) not null")
    private String status;

    @PastOrPresent(message = "Created date cannot be in the future")
    @Column(columnDefinition = "datetime")
    private LocalDateTime createdAt;


}
