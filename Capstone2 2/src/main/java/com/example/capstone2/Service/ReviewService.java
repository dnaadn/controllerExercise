package com.example.capstone2.Service;

import com.example.capstone2.API.ApiException;
import com.example.capstone2.Model.Booking;
import com.example.capstone2.Model.Place;
import com.example.capstone2.Model.Review;
import com.example.capstone2.Model.User;
import com.example.capstone2.Repository.BookingRepository;
import com.example.capstone2.Repository.PlaceRepository;
import com.example.capstone2.Repository.ReviewRepository;
import com.example.capstone2.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final WhatsappService whatsappService;


    public List<Review> getAllReviews(){
        return reviewRepository.findAll();
    }

    public void addReview(Review review) {

        if (userRepository.findUserById(review.getUserId()) == null) {
            throw new ApiException("User not found");
        }

        Booking booking = bookingRepository.findBookingById(review.getBookingId());

        if (booking == null) {
            throw new ApiException("Booking not found");
        }

        if (!booking.getUserId().equals(review.getUserId())) {
            throw new ApiException("This booking does not belong to the user");
        }

        if (!"COMPLETED".equals(booking.getStatus())) {
            throw new ApiException("Only COMPLETED bookings can be reviewed");
        }

        // one review per booking
        if (reviewRepository.existsByBookingId(review.getBookingId())) {
            throw new ApiException("This booking already has a review");
        }

        review.setCreatedAt(LocalDateTime.now());

        reviewRepository.save(review);
        User user = userRepository.findUserById(review.getUserId());
        Place place = placeRepository.findPlaceById(booking.getPlaceId());

        whatsappService.sendMessage(
                user.getPhone(),
                "Thank you for your review! ⭐\n\n"
                        + "Place: " + place.getName() + "\n"
                        + "Rating: " + review.getRating() + "/5\n\n"
                        + "Your feedback helps improve Wusool."
        );

    }

    public void updateReview(Integer id, Review newData) {

        Review oldData = reviewRepository.findReviewById(id);

        if (oldData == null) {
            throw new ApiException("Review not found");
        }
        // the review cannot be moved to another booking or another user
        if (!oldData.getBookingId().equals(newData.getBookingId())
                || !oldData.getUserId().equals(newData.getUserId())) {
            throw new ApiException("The review cannot be moved to another booking or user");
        }

        oldData.setRating(newData.getRating());
        oldData.setComment(newData.getComment());

        reviewRepository.save(oldData);
    }

    public void deleteReview(Integer id) {

        Review review = reviewRepository.findReviewById(id);

        if (review == null) {
            throw new ApiException("Review not found");
        }

        reviewRepository.delete(review);
    }

    // reviews of a place
    public List<Review> getPlaceReviews(Integer placeId) {

        if (placeRepository.findPlaceById(placeId) == null) {
            throw new ApiException("Place not found");
        }

        // a review is linked to a booking, and the booking is linked to the place
        List<Booking> bookings = bookingRepository.findBookingsByPlaceId(placeId);
        List<Review> reviews = new ArrayList<>();

        for (Booking booking : bookings) {
            Review review = reviewRepository.findReviewByBookingId(booking.getId());

            if (review != null) {
                reviews.add(review);
            }
        }

        return reviews;
    }

    // extra: average rating of a place as a sentence, null if the place does not exist
    public String getPlaceAverageRating(Integer placeId) {

        Place place = placeRepository.findPlaceById(placeId);

        if (place == null) {
            throw new ApiException("Place not found");
        }

        List<Review> reviews = getPlaceReviews(placeId);

        if (reviews.isEmpty()) {
            return place.getName() + " has no reviews yet";
        }

        double sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }

        double average = Math.round((sum / reviews.size()) * 10.0) / 10.0;

        return place.getName() + " has an average rating of " + average
                + " out of 5 based on " + reviews.size()
                + (reviews.size() == 1 ? " review" : " reviews");
    }


}
