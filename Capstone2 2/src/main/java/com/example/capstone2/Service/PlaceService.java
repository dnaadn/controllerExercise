package com.example.capstone2.Service;

import com.example.capstone2.API.ApiException;
import com.example.capstone2.Model.Booking;
import com.example.capstone2.Model.Place;
import com.example.capstone2.Model.PlaceAccessibility;
import com.example.capstone2.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final BusinessOwnerRepository businessOwnerRepository;
    private final PlaceCategoryRepository placeCategoryRepository;
    private final BookingRepository bookingRepository;
    private final PlaceImageRepository placeImageRepository;
    private final PlaceAccessibilityRepository placeAccessibilityRepository;
    private final UserRepository userRepository;

    public List<Place> getAllPlaces(){
        return placeRepository.findAll();
    }

    public void addPlace(Place place) {

        if (businessOwnerRepository.findBusinessOwnerById(place.getOwnerId()) == null) {
            throw new ApiException("Business owner not found");
        }

        if (placeCategoryRepository.findPlaceCategoryById(place.getCategoryId()) == null) {
            throw new ApiException("Place category not found");
        }
        if (!hasValidWorkingHours(place)) {
            throw new ApiException("Opening time and closing time cannot be the same");
        }

        placeRepository.save(place);
    }

    public void updatePlace(Integer id, Place newPlace) {

        Place oldPlace = placeRepository.findPlaceById(id);

        if (oldPlace == null) {
            throw new ApiException("Place not found");
        }
        if (businessOwnerRepository.findBusinessOwnerById(newPlace.getOwnerId()) == null) {
            throw new ApiException("Business owner not found");
        }
        if (placeCategoryRepository.findPlaceCategoryById(newPlace.getCategoryId()) == null) {
            throw new ApiException("Place category not found");
        }
        if (!hasValidWorkingHours(newPlace)) {
            throw new ApiException("Opening time and closing time cannot be the same");
        }

        oldPlace.setName(newPlace.getName());
        oldPlace.setDescription(newPlace.getDescription());
        oldPlace.setAddress(newPlace.getAddress());
        oldPlace.setCity(newPlace.getCity());
        oldPlace.setPhone(newPlace.getPhone());
        oldPlace.setOpeningTime(newPlace.getOpeningTime());
        oldPlace.setClosingTime(newPlace.getClosingTime());
        oldPlace.setOwnerId(newPlace.getOwnerId());
        oldPlace.setCategoryId(newPlace.getCategoryId());

        placeRepository.save(oldPlace);


    }

    public void deletePlace(Integer id) {

        Place place = placeRepository.findPlaceById(id);

        if (place == null) {
            throw new ApiException("Place not found");
        }

        if (bookingRepository.existsByPlaceId(id)) {
            throw new ApiException("Cannot delete a place that has bookings");
        }

        // images and accessibility info belong to the place, delete them with it
        placeImageRepository.deleteByPlaceId(id);
        placeAccessibilityRepository.deleteByPlaceId(id);

        placeRepository.delete(place);

    }

    // places that a user visited (he has a COMPLETED booking in them)
    public List<Place> getPlacesVisitedByUser(Integer userId) {

        if (userRepository.findUserById(userId) == null) {
            throw new ApiException("User not found");
        }

        List<Booking> bookings = bookingRepository.findBookingsByUserIdAndStatus(userId, "COMPLETED");
        List<Place> places = new ArrayList<>();

        for (Booking booking : bookings) {
            Place place = placeRepository.findPlaceById(booking.getPlaceId());

            // the user may visit the same place more than once, add it only once
            if (place != null && !places.contains(place)) {
                places.add(place);
            }
        }

        return places;
    }

    public List<Place> getPlacesByCity(String city) {
        return placeRepository.findPlacesByCityIgnoreCase(city);
    }

    public List<Place> getPlacesByCategory(Integer categoryId) {

        if (placeCategoryRepository.findPlaceCategoryById(categoryId) == null) {
            throw new ApiException("Place category not found");
        }

        return placeRepository.findPlacesByCategoryId(categoryId);
    }

    // places that support an accessibility type (ex: wheelchair)
    public List<Place> getPlacesByAccessibility(String type) {

        List<PlaceAccessibility> accessibilities =
                placeAccessibilityRepository.findAccessibilitiesByAccessibilityTypeIgnoreCase(type);
        List<Place> places = new ArrayList<>();

        for (PlaceAccessibility accessibility : accessibilities) {
            Place place = placeRepository.findPlaceById(accessibility.getPlaceId());

            if (place != null && !places.contains(place)) {
                places.add(place);
            }
        }

        return places;
    }

    // places that are open right now
    public List<Place> getOpenPlacesNow() {

        LocalTime now = LocalTime.now();
        List<Place> openPlaces = new ArrayList<>();

        for (Place place : placeRepository.findAll()) {
            if (isOpenAt(place, now)) {
                openPlaces.add(place);
            }
        }

        return openPlaces;
    }

    // supports places that close after midnight (e.g. 18:00 -> 02:00)
    private boolean isOpenAt(Place place, LocalTime time) {

        LocalTime open = place.getOpeningTime();
        LocalTime close = place.getClosingTime();

        if (open.isBefore(close)) {
            return !time.isBefore(open) && time.isBefore(close);
        }

        return !time.isBefore(open) || time.isBefore(close);
    }

    //Get available places for a specific time
    public List<Place> getAvailablePlaces(LocalTime time) {
        return placeRepository.findAvailablePlaces(time);
    }

    public List<Place> getMostVisitedPlaces() {

        List<Object[]> results = bookingRepository.findMostVisitedPlaces();

        List<Place> places = new ArrayList<>();

        for (Object[] result : results) {

            Integer placeId = (Integer) result[0];

            Place place = placeRepository.findPlaceById(placeId);

            if (place != null) {
                places.add(place);
            }
        }

        return places;
    }

    // extra #3: search places by city + a (partial) category name, ex: city=Riyadh, category=cafe
    public List<Place> searchPlaces(String city, String categoryName) {
        return placeRepository.searchPlacesByCityAndCategoryName(city, categoryName);
    }

    // opening and closing time can't be the same
    private boolean hasValidWorkingHours(Place place) {
        return !place.getOpeningTime().equals(place.getClosingTime());
    }
}
