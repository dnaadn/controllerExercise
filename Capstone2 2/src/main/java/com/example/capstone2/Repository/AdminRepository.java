package com.example.capstone2.Repository;

import com.example.capstone2.Model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Admin findAdminById(Integer id);
}
