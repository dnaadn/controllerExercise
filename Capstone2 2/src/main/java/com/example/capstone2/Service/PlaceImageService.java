package com.example.capstone2.Service;

import com.example.capstone2.API.ApiException;
import com.example.capstone2.Model.PlaceImage;
import com.example.capstone2.Repository.PlaceImageRepository;
import com.example.capstone2.Repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class PlaceImageService {

    private final PlaceImageRepository placeImageRepository;
    private final PlaceRepository placeRepository;



    public List<PlaceImage> getAllPlaceImages(){
        return placeImageRepository.findAll();
    }

    public void addPlaceImage(PlaceImage placeImage) {

        if (placeRepository.findPlaceById(placeImage.getPlaceId()) == null) {
            throw new ApiException("Place not found");
        }

        placeImageRepository.save(placeImage);
    }

    public void updatePlaceImage(Integer id, PlaceImage newData) {

        PlaceImage oldData = placeImageRepository.findPlaceImageById(id);

        if (oldData == null) {
            throw new ApiException("Place image not found");
        }

        if (placeRepository.findPlaceById(newData.getPlaceId()) == null) {
            throw new ApiException("Place not found");

        }

        oldData.setPlaceId(newData.getPlaceId());
        oldData.setImageUrl(newData.getImageUrl());

        placeImageRepository.save(oldData);
    }

    public void deletePlaceImage(Integer id) {

        PlaceImage placeImage = placeImageRepository.findPlaceImageById(id);

        if (placeImage == null) {
            throw new ApiException("Place image not found");
        }

        placeImageRepository.delete(placeImage);
    }
}
