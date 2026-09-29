package com.example.capstone2.Repository;

import com.example.capstone2.Model.PlaceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceCategoryRepository extends JpaRepository<PlaceCategory, Integer> {

    PlaceCategory findPlaceCategoryById(Integer id);

    boolean existsByNameIgnoreCase(String name);

}
