package com.example.capstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PlaceAccessibility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Place id is required")
    @Column(columnDefinition = "int not null")
    private Integer placeId;

    @NotBlank(message = "Accessibility type is required")
    @Size(max = 50, message = "Accessibility type must not exceed 50 characters")
    @Column(columnDefinition = "varchar(50) not null")
    private String accessibilityType;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    @Column(columnDefinition = "varchar(255)")
    private String description;
}
