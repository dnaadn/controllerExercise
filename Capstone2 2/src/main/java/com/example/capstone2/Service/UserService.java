package com.example.capstone2.Service;

import com.example.capstone2.API.ApiException;
import com.example.capstone2.Model.User;
import com.example.capstone2.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void addUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ApiException("Email already exists");
        }
        if (userRepository.existsByPhone(user.getPhone())) {
            throw new ApiException("Phone number already exists");
        }
        userRepository.save(user);
    }


    public void updateUser(Integer id, User newUser) {

        User oldUser = userRepository.findUserById(id);

        if (oldUser == null) {
            throw new ApiException("User not found");
        }

        if (!oldUser.getEmail().equals(newUser.getEmail())
                && userRepository.existsByEmail(newUser.getEmail())) {
            throw new ApiException("Email already exists");
        }

        if (!oldUser.getPhone().equals(newUser.getPhone())
                && userRepository.existsByPhone(newUser.getPhone())) {
            throw new ApiException("Phone number already exists");
        }

        oldUser.setName(newUser.getName());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setPassword(newUser.getPassword());
        oldUser.setPhone(newUser.getPhone());

        userRepository.save(oldUser);

    }

    public void deleteUser(Integer id) {

        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new ApiException("User not found");
        }
        userRepository.delete(user);
    }


    public User login(String email, String password) {

        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new ApiException("User not found");

        }

        if (!user.getPassword().equals(password)) {
            throw new ApiException("Invalid email or password");
        }

        emailService.sendLoginNotification(
                user.getEmail(),
                user.getName()
        );

        return user;
    }

}
