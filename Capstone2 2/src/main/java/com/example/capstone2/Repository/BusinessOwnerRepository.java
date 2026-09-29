package com.example.capstone2.Repository;

import com.example.capstone2.Model.BusinessOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessOwnerRepository extends JpaRepository<BusinessOwner, Integer> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    BusinessOwner findBusinessOwnerById(Integer id);

    boolean existsByBusinessNameIgnoreCase(String businessName);
}
