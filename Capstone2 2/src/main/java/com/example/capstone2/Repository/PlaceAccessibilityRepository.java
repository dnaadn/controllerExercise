package com.example.capstone2.Repository;

import com.example.capstone2.Model.PlaceAccessibility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceAccessibilityRepository extends JpaRepository<PlaceAccessibility, Integer> {

    PlaceAccessibility findPlaceAccessibilityById(Integer id);

    void deleteByPlaceId(Integer placeId);

    List<PlaceAccessibility> findAccessibilitiesByAccessibilityTypeIgnoreCase(String accessibilityType);

}
