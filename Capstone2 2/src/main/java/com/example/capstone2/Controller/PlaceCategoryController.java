package com.example.capstone2.Controller;

import com.example.capstone2.API.ApiResponse;
import com.example.capstone2.Model.PlaceCategory;
import com.example.capstone2.Service.PlaceCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/place-category")
@RequiredArgsConstructor
public class PlaceCategoryController {

    private final PlaceCategoryService placeCategoryService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllPlaceCategories() {
        return ResponseEntity.status(200).body(placeCategoryService.getAllPlaceCategories());
    }

    @PostMapping("/add/{adminId}")
    public ResponseEntity<?> addPlaceCategory(@PathVariable Integer adminId, @RequestBody @Valid PlaceCategory placeCategory) {
        placeCategoryService.addPlaceCategory(adminId, placeCategory);
        return ResponseEntity.status(200).body(new ApiResponse("Place category added successfully"));

    }

    @PutMapping("/update/{adminId}/{id}")
    public ResponseEntity<?> updatePlaceCategory(@PathVariable Integer adminId, @PathVariable Integer id, @RequestBody @Valid PlaceCategory placeCategory) {
        placeCategoryService.updatePlaceCategory(adminId, id, placeCategory);
        return ResponseEntity.status(200).body(new ApiResponse("Place category updated successfully"));

    }

    @DeleteMapping("/delete/{adminId}/{id}")
    public ResponseEntity<?> deletePlaceCategory(@PathVariable Integer adminId, @PathVariable Integer id) {
        placeCategoryService.deletePlaceCategory(adminId, id);
        return ResponseEntity.status(200).body(new ApiResponse("Place category deleted successfully"));
        }


}
