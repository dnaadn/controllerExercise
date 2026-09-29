package com.example.capstone2.Service;

import com.example.capstone2.API.ApiException;
import com.example.capstone2.Model.Booking;
import com.example.capstone2.Model.Place;
import com.example.capstone2.Model.User;
import com.example.capstone2.Repository.BookingRepository;
import com.example.capstone2.Repository.PlaceRepository;
import com.example.capstone2.Repository.ReviewRepository;
import com.example.capstone2.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;
    private final EmailService emailService;
    private final WhatsappService whatsappService;


    public List<Booking> getAllBookings(){
        return bookingRepository.findAll();
    }

    public void addBooking(Booking booking) {

        if (userRepository.findUserById(booking.getUserId()) == null) {
            throw new ApiException("User not found");
        }

        Place place = placeRepository.findPlaceById(booking.getPlaceId());

        if (place == null) {
            throw new ApiException("Place not found");
        }

        if (!isWithinWorkingHours(place, booking.getBookingTime())) {
            throw new ApiException("Booking time is outside the working hours of the place");
        }

        // the booking must not be in the past
        LocalDateTime requested = LocalDateTime.of(booking.getBookingDate(), booking.getBookingTime());
        if (requested.isBefore(LocalDateTime.now())) {
            throw new ApiException("Booking date and time cannot be in the past");

        }

        // the place must be free at this date and time
        if (bookingRepository.isSlotTaken(booking.getPlaceId(), booking.getBookingDate(), booking.getBookingTime())) {
            throw new ApiException("This time slot is already booked");
        }

        // a new booking always starts as PENDING
        booking.setStatus("PENDING");
        booking.setCreatedAt(LocalDateTime.now());

        bookingRepository.save(booking);

        User user = userRepository.findUserById(booking.getUserId());

        whatsappService.sendMessage(
                user.getPhone(),
                "Welcome to Wusool! 👋\n\n"
                        + "Your booking request has been received.\n\n"
                        + "Place: " + place.getName() + "\n"
                        + "Date: " + booking.getBookingDate() + "\n"
                        + "Time: " + booking.getBookingTime() + "\n"
                        + "Status: PENDING"
        );


    }


    public void updateBooking(Integer id, Booking newData) {

        Booking oldData = bookingRepository.findBookingById(id);

        if (oldData == null) {
            throw new ApiException("Booking not found");
        }

        if (userRepository.findUserById(newData.getUserId()) == null) {
            throw new ApiException("User not found");

        }

        Place place = placeRepository.findPlaceById(newData.getPlaceId());

        if (place == null) {
            throw new ApiException("Place not found");
        }

        if (!isWithinWorkingHours(place, newData.getBookingTime())) {
            throw new ApiException("Booking time is outside the working hours of the place");
        }

        // the place must be free, ignoring this same booking
        if (bookingRepository.isSlotTakenExcluding(newData.getPlaceId(), newData.getBookingDate(), newData.getBookingTime(), id)) {
            throw new ApiException("This time slot is already booked");
        }

        oldData.setUserId(newData.getUserId());
        oldData.setPlaceId(newData.getPlaceId());
        oldData.setBookingDate(newData.getBookingDate());
        oldData.setBookingTime(newData.getBookingTime());
        oldData.setStatus(newData.getStatus());

        bookingRepository.save(oldData);
        User user = userRepository.findUserById(oldData.getUserId());

        whatsappService.sendMessage(
                user.getPhone(),
                "Your booking has been updated successfully! ✏️\n\n"
                        + "Place: " + place.getName() + "\n"
                        + "Date: " + oldData.getBookingDate() + "\n"
                        + "Time: " + oldData.getBookingTime()
        );
    }


    public void deleteBooking(Integer id) {

        Booking booking = bookingRepository.findBookingById(id);

        if (booking == null) {
            throw new ApiException("Booking not found");
        }

        if (reviewRepository.existsByBookingId(id)) {
            throw new ApiException("Cannot delete a booking that already has a review");
        }
        User user = userRepository.findUserById(booking.getUserId());
        Place place = placeRepository.findPlaceById(booking.getPlaceId());

        bookingRepository.delete(booking);

        // WhatsApp notification
        if (user != null && place != null) {

            whatsappService.sendMessage(
                    user.getPhone(),
                    "Your booking has been cancelled. ❌\n\n"
                            + "Place: " + place.getName() + "\n"
                            + "Date: " + booking.getBookingDate() + "\n"
                            + "Time: " + booking.getBookingTime()
            );
        }

    }

    // booking history of a user, newest first
    public List<Booking> getUserBookingHistory(Integer userId) {

        if (userRepository.findUserById(userId) == null) {
            throw new ApiException("User not found");
        }

        return bookingRepository.getUserBookingHistory(userId);
    }

    // extra: the owner of the place marks the booking as COMPLETED, after that the user can review it
    public void completeBooking(Integer ownerId, Integer bookingId) {

        Booking booking = bookingRepository.findBookingById(bookingId);

        if (booking == null) {
            throw new ApiException("Booking not found");
        }

        Place place = placeRepository.findPlaceById(booking.getPlaceId());

        if (place == null) {
            throw new ApiException("Place not found");
        }

        // only the owner of the place can complete its bookings
        if (!place.getOwnerId().equals(ownerId)) {
            throw new ApiException("Only the owner of the place can complete its bookings");
        }

        if (!"PENDING".equals(booking.getStatus())) {
            throw new ApiException("Only PENDING bookings can be completed");
        }

        booking.setStatus("COMPLETED");
        bookingRepository.save(booking);

        // send the confirmation email automatically once the owner completes the booking
        User user = userRepository.findUserById(booking.getUserId());

        if (user != null) {

            emailService.sendBookingConfirmation(
                    user.getEmail(),
                    place.getName(),
                    booking.getBookingDate().toString(),
                    booking.getBookingTime().toString()
            );

            whatsappService.sendMessage(
                    user.getPhone(),
                    "Your booking has been confirmed! ✅\n\n"
                            + "Place: " + place.getName() + "\n"
                            + "Date: " + booking.getBookingDate() + "\n"
                            + "Time: " + booking.getBookingTime() + "\n\n"
                            + "Thank you for using Wusool."
            );

        }

    }

    // extra: bookings of a place, if the date is null return all of them
    public List<Booking> getPlaceBookings(Integer placeId, LocalDate date) {

        if (placeRepository.findPlaceById(placeId) == null) {
            throw new ApiException("Place not found");
        }

        if (date == null) {
            return bookingRepository.findBookingsByPlaceId(placeId);
        }

        return bookingRepository.findBookingsByPlaceIdAndBookingDate(placeId, date);
    }


    // the booking time must be between the opening and closing time of the place
    private boolean isWithinWorkingHours(Place place, LocalTime time) {
        return !time.isBefore(place.getOpeningTime()) && !time.isAfter(place.getClosingTime());
    }
}
