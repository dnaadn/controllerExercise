package com.example.capstone2.Controller;

import com.example.capstone2.API.ApiResponse;
import com.example.capstone2.Model.Review;
import com.example.capstone2.Service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllReviews() {
        return ResponseEntity.status(200).body(reviewService.getAllReviews());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestBody @Valid Review review) {
       reviewService.addReview(review);
       return ResponseEntity.status(200).body(new ApiResponse("Review added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Integer id, @RequestBody @Valid Review review) {
        reviewService.updateReview(id, review);
        return ResponseEntity.status(200).body(new ApiResponse("Review updated successfully"));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer id) {
        reviewService.deleteReview(id);
        return ResponseEntity.status(200).body(new ApiResponse("Review deleted successfully"));
    }

    // extra: reviews of a place
    @GetMapping("/place/{placeId}")
    public ResponseEntity<?> getPlaceReviews(@PathVariable Integer placeId) {
        return ResponseEntity.status(200).body(reviewService.getPlaceReviews(placeId));
    }

    // extra: average rating of a place
    @GetMapping("/place/{placeId}/average")
    public ResponseEntity<?> getPlaceAverageRating(@PathVariable Integer placeId) {
        String message = reviewService.getPlaceAverageRating(placeId);
        return ResponseEntity.status(200).body(new ApiResponse(message));
    }
}
