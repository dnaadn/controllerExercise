package com.example.capstone2.Service;

import com.example.capstone2.API.ApiException;
import com.example.capstone2.Model.PlaceAccessibility;
import com.example.capstone2.Repository.PlaceAccessibilityRepository;
import com.example.capstone2.Repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceAccessibilityService {

    private final PlaceAccessibilityRepository placeAccessibilityRepository;
    private final PlaceRepository placeRepository;

    public List<PlaceAccessibility> getAllPlaceAccessibility(){
        return placeAccessibilityRepository.findAll();
    }

    public void addPlaceAccessibility(PlaceAccessibility placeAccessibility) {

        if (placeRepository.findPlaceById(placeAccessibility.getPlaceId()) == null) {
            throw new ApiException("Place not found");
        }
        placeAccessibilityRepository.save(placeAccessibility);
    }

    public void updatePlaceAccessibility(Integer id, PlaceAccessibility newData) {

        PlaceAccessibility oldData = placeAccessibilityRepository.findPlaceAccessibilityById(id);

        if (oldData == null) {
            throw new ApiException("Place accessibility not found");
        }

        if (placeRepository.findPlaceById(newData.getPlaceId()) == null) {
            throw new ApiException("Place not found");
        }

        oldData.setPlaceId(newData.getPlaceId());
        oldData.setAccessibilityType(newData.getAccessibilityType());
        oldData.setDescription(newData.getDescription());

        placeAccessibilityRepository.save(oldData);

    }

    public void deletePlaceAccessibility(Integer id) {
        PlaceAccessibility placeAccessibility = placeAccessibilityRepository.findPlaceAccessibilityById(id);
        if (placeAccessibility == null) {
            throw new ApiException("Place accessibility not found");

        }
        placeAccessibilityRepository.delete(placeAccessibility);
    }
}
