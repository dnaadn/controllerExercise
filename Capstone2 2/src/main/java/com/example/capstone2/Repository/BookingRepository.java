package com.example.capstone2.Repository;

import com.example.capstone2.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Booking findBookingById(Integer id);
    boolean existsByPlaceId(Integer placeId);

    // is there a booking in the same place, on the same date, at the same time (used when adding)
    @Query("select count(b) > 0 from Booking b where b.placeId = ?1 and b.bookingDate = ?2 and b.bookingTime = ?3")
    boolean isSlotTaken(Integer placeId, LocalDate date, LocalTime time);


    @Query("select count(b) > 0 from Booking b where b.placeId = ?1 and b.bookingDate = ?2 and b.bookingTime = ?3 and b.id <> ?4")
    boolean isSlotTakenExcluding(Integer placeId, LocalDate date, LocalTime time, Integer id);

    // booking history of a user, newest first
    @Query("select b from Booking b where b.userId = ?1 order by b.bookingDate desc, b.bookingTime desc")
    List<Booking> getUserBookingHistory(Integer userId);

    List<Booking> findBookingsByPlaceId(Integer placeId);

    List<Booking> findBookingsByPlaceIdAndBookingDate(Integer placeId, LocalDate bookingDate);

    List<Booking> findBookingsByUserIdAndStatus(Integer userId, String status);

    @Query("select b.placeId, count(b) from Booking b where b.status = 'COMPLETED' group by b.placeId order by count(b) desc")
    List<Object[]> findMostVisitedPlaces();



}
