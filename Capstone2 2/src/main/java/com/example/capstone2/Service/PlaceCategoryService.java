package com.example.capstone2.Service;

import com.example.capstone2.API.ApiException;
import com.example.capstone2.Model.PlaceCategory;
import com.example.capstone2.Repository.AdminRepository;
import com.example.capstone2.Repository.PlaceCategoryRepository;
import com.example.capstone2.Repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceCategoryService {

    private final PlaceCategoryRepository placeCategoryRepository;
    private final PlaceRepository placeRepository;
    private final AdminRepository adminRepository;


    public List<PlaceCategory> getAllPlaceCategories(){
        return placeCategoryRepository.findAll();
    }

    public void addPlaceCategory(Integer adminId, PlaceCategory placeCategory) {

        // only an admin can manage categories
        if (adminRepository.findAdminById(adminId) == null) {
            throw new ApiException("Admin not found");
        }
        if (placeCategoryRepository.existsByNameIgnoreCase(placeCategory.getName())) {
            throw new ApiException("Category name already exists");
        }

        placeCategoryRepository.save(placeCategory);
    }

    public void updatePlaceCategory(Integer adminId, Integer id, PlaceCategory newData) {

        if (adminRepository.findAdminById(adminId) == null) {
            throw new ApiException("Admin not found");
        }

        PlaceCategory oldData = placeCategoryRepository.findPlaceCategoryById(id);

        if (oldData == null) {
            throw new ApiException("Place category not found");
        }

        if (!oldData.getName().equalsIgnoreCase(newData.getName())
                && placeCategoryRepository.existsByNameIgnoreCase(newData.getName())) {
            throw new ApiException("Category name already exists");
        }

        oldData.setName(newData.getName());
        oldData.setDescription(newData.getDescription());

        placeCategoryRepository.save(oldData);

    }

    public void deletePlaceCategory(Integer adminId, Integer id) {

        if (adminRepository.findAdminById(adminId) == null) {
            throw new ApiException("Admin not found");
        }

        PlaceCategory placeCategory = placeCategoryRepository.findPlaceCategoryById(id);

        if (placeCategory == null) {
            throw new ApiException("Place category not found");
        }

        // a category used by places can't be deleted
        if (placeRepository.existsByCategoryId(id)) {
            throw new ApiException("Cannot delete a category that is used by places");
        }

        placeCategoryRepository.delete(placeCategory);
    }
}
