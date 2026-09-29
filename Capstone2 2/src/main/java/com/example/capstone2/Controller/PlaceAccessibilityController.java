package com.example.capstone2.Controller;

import com.example.capstone2.API.ApiResponse;
import com.example.capstone2.Model.PlaceAccessibility;
import com.example.capstone2.Service.PlaceAccessibilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/place-accessibility")
@RequiredArgsConstructor
public class PlaceAccessibilityController {

    private final PlaceAccessibilityService placeAccessibilityService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllPlaceAccessibility() {
        return ResponseEntity.status(200).body(placeAccessibilityService.getAllPlaceAccessibility());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPlaceAccessibility(@RequestBody @Valid PlaceAccessibility placeAccessibility) {
        placeAccessibilityService.addPlaceAccessibility(placeAccessibility);
        return ResponseEntity.status(200).body(new ApiResponse("Place accessibility added successfully"));

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePlaceAccessibility(@PathVariable Integer id, @RequestBody @Valid PlaceAccessibility placeAccessibility) {
       placeAccessibilityService.updatePlaceAccessibility(id, placeAccessibility);
       return ResponseEntity.status(200).body(new ApiResponse("Place accessibility updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePlaceAccessibility(@PathVariable Integer id) {
        placeAccessibilityService.deletePlaceAccessibility(id);
        return ResponseEntity.status(200).body(new ApiResponse("Place accessibility deleted successfully"));
    }
}
