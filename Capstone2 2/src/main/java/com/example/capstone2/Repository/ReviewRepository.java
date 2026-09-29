package com.example.capstone2.Repository;

import com.example.capstone2.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Review findReviewById(Integer id);
    boolean existsByBookingId(Integer bookingId);
    Review findReviewByBookingId(Integer bookingId);

}
