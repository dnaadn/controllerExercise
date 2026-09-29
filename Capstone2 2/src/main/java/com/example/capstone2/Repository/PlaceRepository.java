package com.example.capstone2.Repository;

import com.example.capstone2.Model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Integer> {

    Place findPlaceById(Integer id);

    List<Place> findPlacesByCityIgnoreCase(String city);

    boolean existsByCategoryId(Integer categoryId);

    List<Place> findPlacesByCategoryId(Integer categoryId);

    boolean existsByOwnerId(Integer ownerId);

    @Query("select p from Place p where p.openingTime <= ?1 and p.closingTime >= ?1")
    List<Place> findAvailablePlaces(LocalTime time);

    // extra #3: search places by city and a (partial) category name, e.g. city="Riyadh", category="cafe"
    @Query("select p from Place p, PlaceCategory c where p.categoryId = c.id and p.city = ?1 and c.name like %?2%")
    List<Place> searchPlacesByCityAndCategoryName(String city, String categoryName);



}
