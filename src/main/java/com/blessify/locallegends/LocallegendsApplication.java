package com.blessify.locallegends;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.blessify.locallegends.service.EmailSender;

@SpringBootApplication
public class LocallegendsApplication {

	@Autowired
	private EmailSender emailSender;

	public static void main(String[] args) {
		SpringApplication.run(LocallegendsApplication.class, args);
	}
	@Bean
	public CommandLineRunner testSendEmail() {
		return args -> {
			emailSender.sendWelcomeEmail("youngtips23@gmail.com", "Tadiwa");
			emailSender.sendAdminNotification("youngtips", "user");

		};
	}

}
