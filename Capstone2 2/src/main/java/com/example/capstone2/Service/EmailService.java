package com.example.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendBookingConfirmation(
            String email,
            String placeName,
            String date,
            String time) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Wusool Booking Confirmation");

        message.setText(
                "Hello,\n\n" +
                        "Your booking has been confirmed.\n\n" +
                        "Place: " + placeName + "\n" +
                        "Date: " + date + "\n" +
                        "Time: " + time + "\n\n" +
                        "Thank you for using Wusool."
        );

        mailSender.send(message);
    }
    public void sendLoginNotification(String email, String name) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Wusool - Login Notification");

        message.setText(
                "Hello " + name + ",\n\n"
                        + "You have successfully logged in to your Wusool account.\n\n"
                        + "If this was not you, please check your account.\n\n"
                        + "Thank you for using Wusool."
        );

        mailSender.send(message);
    }
}
