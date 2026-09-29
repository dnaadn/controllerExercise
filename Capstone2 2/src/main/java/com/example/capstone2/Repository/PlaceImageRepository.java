package com.example.capstone2.Repository;

import com.example.capstone2.Model.PlaceImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceImageRepository extends JpaRepository<PlaceImage, Integer> {

    PlaceImage findPlaceImageById(Integer id);

    void deleteByPlaceId(Integer placeId);

}
