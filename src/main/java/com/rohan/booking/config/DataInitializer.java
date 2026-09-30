package com.rohan.booking.config;

import com.rohan.booking.entity.User;
import com.rohan.booking.enums.Role;
import com.rohan.booking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (!userRepository.existsByUsername("admin")) {

                User admin = new User(
                        "admin",
                        passwordEncoder.encode("Admin@123"),
                        Role.ADMIN
                );

                userRepository.save(admin);
            }

            if (!userRepository.existsByUsername("user")) {

                User user = new User(
                        "user",
                        passwordEncoder.encode("User@123"),
                        Role.USER
                );

                userRepository.save(user);
            }
        };
    }
}