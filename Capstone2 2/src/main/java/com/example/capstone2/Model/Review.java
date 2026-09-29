package com.example.capstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Booking id is required")
    @Column(columnDefinition = "int not null")
    private Integer bookingId;

    @NotNull(message = "User id is required")
    @Column(columnDefinition = "int not null")
    private Integer userId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    @Column(columnDefinition = "int not null")
    private Integer rating;

    @Size(max = 255, message = "Comment must not exceed 255 characters")
    @Column(columnDefinition = "varchar(255)")
    private String comment;

    @PastOrPresent(message = "Created date cannot be in the future")
    @Column(columnDefinition = "datetime")
    private LocalDateTime createdAt;
}
