package com.example.capstone2.Controller;

import com.example.capstone2.API.ApiResponse;
import com.example.capstone2.Model.Booking;
import com.example.capstone2.Service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllBookings() {
        return ResponseEntity.status(200).body(bookingService.getAllBookings());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addBooking(@RequestBody @Valid Booking booking) {
        bookingService.addBooking(booking);
        return ResponseEntity.status(200).body(new ApiResponse("Booking added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Integer id, @RequestBody @Valid Booking booking) {
       bookingService.updateBooking(id, booking);
       return ResponseEntity.status(200).body(new ApiResponse("Booking updated successfully"));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Integer id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.status(200).body(new ApiResponse("Booking deleted successfully"));
    }

    // extra: booking history of a user (newest first)
    @GetMapping("/user-history/{userId}")
    public ResponseEntity<?> getUserBookingHistory(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(bookingService.getUserBookingHistory(userId));
    }

    // extra: the owner completes a booking (PENDING -> COMPLETED)
    @PutMapping("/complete/{ownerId}/{bookingId}")
    public ResponseEntity<?> completeBooking(@PathVariable Integer ownerId, @PathVariable Integer bookingId) {
        bookingService.completeBooking(ownerId, bookingId);
        return ResponseEntity.status(200).body(new ApiResponse("Booking completed successfully"));
    }

    // extra: bookings of a place on a specific date
    @GetMapping("/place/{placeId}/{date}")
    public ResponseEntity<?> getPlaceBookings(@PathVariable Integer placeId, @PathVariable String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        return ResponseEntity.status(200).body(bookingService.getPlaceBookings(placeId, parsedDate));
    }


}
