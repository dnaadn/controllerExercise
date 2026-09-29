package com.example.capstone2.Service;

import com.example.capstone2.API.ApiException;
import com.example.capstone2.Model.Admin;
import com.example.capstone2.Repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    public List<Admin> getAllAdmins(){
        return adminRepository.findAll();
    }

    public void addAdmin(Admin admin){
        if(adminRepository.existsByEmail(admin.getEmail())){
            throw new ApiException("Email already exists");
        }
        if(adminRepository.existsByPhone(admin.getPhone())){
            throw new ApiException("Phone number already exists");
        }

        adminRepository.save(admin);

    }

    public void updateAdmin(Integer id, Admin newAdmin){
        Admin oldAdmin = adminRepository.findAdminById(id);

        if(oldAdmin == null){
            throw new ApiException("Admin not found");
        }
        if (!oldAdmin.getEmail().equals(newAdmin.getEmail())
                && adminRepository.existsByEmail(newAdmin.getEmail())) {
            throw new ApiException("Email already exists");
        }

        if (!oldAdmin.getPhone().equals(newAdmin.getPhone())
                && adminRepository.existsByPhone(newAdmin.getPhone())) {
            throw new ApiException("Phone number already exists");
        }

        oldAdmin.setName(newAdmin.getName());
        oldAdmin.setEmail(newAdmin.getEmail());
        oldAdmin.setPassword(newAdmin.getPassword());
        oldAdmin.setPhone(newAdmin.getPhone());

        adminRepository.save(oldAdmin);
    }

    public void deleteAdmin(Integer id){
        Admin admin = adminRepository.findAdminById(id);

        if(admin == null){
            throw new ApiException("Admin not found");
        }
        adminRepository.delete(admin);
    }


}
