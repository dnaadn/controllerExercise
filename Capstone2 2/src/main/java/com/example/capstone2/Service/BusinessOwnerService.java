package com.example.capstone2.Service;

import com.example.capstone2.API.ApiException;
import com.example.capstone2.Model.BusinessOwner;
import com.example.capstone2.Repository.BusinessOwnerRepository;
import com.example.capstone2.Repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessOwnerService {

    private final BusinessOwnerRepository businessOwnerRepository;
    private final PlaceRepository placeRepository;


    public List<BusinessOwner> getAllBusinessOwners(){
        return businessOwnerRepository.findAll();
    }

    public void addBusinessOwner(BusinessOwner businessOwner){
        if (businessOwnerRepository.existsByEmail(businessOwner.getEmail())){
            throw new ApiException("Email already exists");
        }
        if(businessOwnerRepository.existsByPhone(businessOwner.getPhone())){
            throw new ApiException("Phone number already exists");
        }
        if(businessOwnerRepository.existsByBusinessNameIgnoreCase(businessOwner.getBusinessName())){
            throw new ApiException("Business name already exists");
        }

        businessOwnerRepository.save(businessOwner);

    }


    public void updateBusinessOwner(Integer id, BusinessOwner newBusinessOwner){
        BusinessOwner oldBusinessOwner = businessOwnerRepository.findBusinessOwnerById(id);
        if (oldBusinessOwner == null) {
            throw new ApiException("Business owner not found");
        }

        if (!oldBusinessOwner.getEmail().equals(newBusinessOwner.getEmail())
                && businessOwnerRepository.existsByEmail(newBusinessOwner.getEmail())) {
            throw new ApiException("Email already exists");
        }

        if (!oldBusinessOwner.getPhone().equals(newBusinessOwner.getPhone())
                && businessOwnerRepository.existsByPhone(newBusinessOwner.getPhone())) {
            throw new ApiException("Phone number already exists");
        }

        if (!oldBusinessOwner.getBusinessName().equals(newBusinessOwner.getBusinessName())
                && businessOwnerRepository.existsByBusinessNameIgnoreCase(newBusinessOwner.getBusinessName())) {
            throw new ApiException("Business name already exists");
        }

        oldBusinessOwner.setName(newBusinessOwner.getName());
        oldBusinessOwner.setEmail(newBusinessOwner.getEmail());
        oldBusinessOwner.setPassword(newBusinessOwner.getPassword());
        oldBusinessOwner.setPhone(newBusinessOwner.getPhone());
        oldBusinessOwner.setBusinessName(newBusinessOwner.getBusinessName());

        businessOwnerRepository.save(oldBusinessOwner);
    }

    public void deleteBusinessOwner(Integer id ){
        BusinessOwner businessOwner = businessOwnerRepository.findBusinessOwnerById(id);

        if(businessOwner == null){
            throw new ApiException("Business owner not found");
        }
        // an owner who still has places can't be deleted
        if (placeRepository.existsByOwnerId(id)) {
            throw new ApiException("Cannot delete a business owner who still has places");
        }
        businessOwnerRepository.delete(businessOwner);
    }
}
