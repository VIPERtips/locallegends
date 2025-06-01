package com.blessify.locallegends.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    private static final String SUPPORT_EMAIL = "support@locallegends.zim";
    private static final String SUPPORT_LINK = "https://locallegends.zim";
    private static final String BUSINESS_NAME = "Local Legends";
    private String adminEmail = "admin@locallegends.zim";

    public void sendEmail(String toEmail, String subject, String body) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(new InternetAddress(SUPPORT_EMAIL, BUSINESS_NAME));
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true);

            javaMailSender.send(message);
            System.out.println("Email sent successfully to " + toEmail);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public void sendRegistrationSuccessEmail(String userEmail, String username) {
        String subject = BUSINESS_NAME + " - Registration Successful";

        String body = String.format(
            "<html><body style='font-family: Arial, sans-serif; background-color: #f3f4f6;'>" +
            "<div style='background: linear-gradient(to right, #005A3C, #00874B); color: white; padding: 20px; text-align: center;'>" +
            "<h2>%s</h2></div><div style='padding: 30px;'>" +
            "<p>Hi %s,</p><p>Welcome to %s! Your registration was successful.</p>" +
            "<p>You can now log in and start exploring amazing local businesses and events near you.</p>" +
            "<p><a href='%s' style='background: #FFD700; color: #005A3C; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Visit Local Legends</a></p>" +
            "<p>Cheers,<br>%s Team</p></div></body></html>",
            BUSINESS_NAME, username, BUSINESS_NAME, SUPPORT_LINK, BUSINESS_NAME
        );

        sendEmail(userEmail, subject, body);
    }

    public void sendAdminNotification(String userEmail) {
        String subject = BUSINESS_NAME + " - New User Registration Alert";

        String body = String.format(
            "<html><body style='font-family: Arial, sans-serif; background-color: #f3f4f6;'>" +
            "<div style='background: linear-gradient(to right, #FFD700, #FFB300); color: #005A3C; padding: 20px; text-align: center;'>" +
            "<h2>%s Admin Alert</h2></div><div style='padding: 30px;'>" +
            "<p>Hello Admin,</p><p>A new user has just registered:</p>" +
            "<h3 style='color: #005A3C;'>%s</h3>" +
            "<p>Time to get them engaged!</p>" +
            "<p>%s Team</p></div></body></html>",
            BUSINESS_NAME, userEmail, BUSINESS_NAME
        );

        sendEmail(adminEmail, subject, body);
    }
}
