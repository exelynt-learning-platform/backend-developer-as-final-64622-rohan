package com.rohan.booking.config;

import com.rohan.booking.entity.User;
import com.rohan.booking.enums.Role;
import com.rohan.booking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
public class DataInitializer {

    @Bean
    public CommandLineRunner initUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            String adminPassword =
                    System.getenv("ADMIN_PASSWORD");

            String userPassword =
                    System.getenv("USER_PASSWORD");

            if (adminPassword == null || adminPassword.isBlank()) {
                throw new IllegalStateException(
                        "ADMIN_PASSWORD environment variable is required");
            }

            if (userPassword == null || userPassword.isBlank()) {
                throw new IllegalStateException(
                        "USER_PASSWORD environment variable is required");
            }

            if (!userRepository.existsByUsername("admin")) {

                userRepository.save(
                        new User(
                                "admin",
                                passwordEncoder.encode(adminPassword),
                                Role.ADMIN
                        )
                );
            }

            if (!userRepository.existsByUsername("user")) {

                userRepository.save(
                        new User(
                                "user",
                                passwordEncoder.encode(userPassword),
                                Role.USER
                        )
                );
            }
        };
    }
}