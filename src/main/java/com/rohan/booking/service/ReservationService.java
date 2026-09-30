package com.rohan.booking.service;

import com.rohan.booking.dto.reservation.ReservationRequest;
import com.rohan.booking.dto.reservation.ReservationResponse;
import com.rohan.booking.dto.reservation.ReservationUpdateRequest;
import com.rohan.booking.entity.Reservation;
import com.rohan.booking.entity.Resource;
import com.rohan.booking.entity.User;
import com.rohan.booking.enums.ReservationStatus;
import com.rohan.booking.exception.ResourceNotFoundException;
import com.rohan.booking.exception.UserNotFoundException;
import com.rohan.booking.repository.ReservationRepository;
import com.rohan.booking.repository.ResourceRepository;
import com.rohan.booking.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ResourceRepository resourceRepository,
            UserRepository userRepository) {

        this.reservationRepository = reservationRepository;
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
    }

    public ReservationResponse createReservation(
            ReservationRequest request,
            String username) {

        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new IllegalArgumentException(
                    "End time must be after start time");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        Resource resource = resourceRepository.findById(
                        request.getResourceId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Resource not found"));

        if (!resource.isAvailable()) {
            throw new IllegalArgumentException(
                    "Resource is not available");
        }

        Reservation reservation = new Reservation();

        reservation.setUser(user);
        reservation.setResource(resource);
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());

        reservation.setPrice(resource.getPrice());

        reservation.setStatus(ReservationStatus.PENDING);

        LocalDateTime now = LocalDateTime.now();

        reservation.setCreatedAt(now);
        reservation.setUpdatedAt(now);

        Reservation savedReservation =
                reservationRepository.save(reservation);

        return mapToResponse(savedReservation);
    }

    public Page<ReservationResponse> getReservations(
            String username,
            boolean isAdmin,
            ReservationStatus status,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size,
            String sortBy,
            String sortDir) {

        if (minPrice != null
                && minPrice.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "minPrice cannot be negative");
        }

        if (maxPrice != null
                && maxPrice.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "maxPrice cannot be negative");
        }

        if (minPrice != null
                && maxPrice != null
                && minPrice.compareTo(maxPrice) > 0) {

            throw new IllegalArgumentException(
                    "minPrice cannot be greater than maxPrice");
        }

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page must be greater than or equal to 0");
        }

        if (size <= 0 || size > 100) {
            throw new IllegalArgumentException(
                    "Size must be between 1 and 100");
        }

        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "createdAt";
        }

        if (!sortBy.equals("id")
                && !sortBy.equals("startTime")
                && !sortBy.equals("endTime")
                && !sortBy.equals("price")
                && !sortBy.equals("status")
                && !sortBy.equals("createdAt")
                && !sortBy.equals("updatedAt")) {

            throw new IllegalArgumentException(
                    "Invalid sortBy field");
        }

        if (sortDir == null || sortDir.isBlank()) {
            sortDir = "desc";
        }

        if (!sortDir.equalsIgnoreCase("asc")
                && !sortDir.equalsIgnoreCase("desc")) {

            throw new IllegalArgumentException(
                    "sortDir must be 'asc' or 'desc'");
        }

        Sort sort;

        if (sortDir.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable =
                PageRequest.of(page, size, sort);

        Page<Reservation> reservations;

        if (isAdmin) {

            if (status != null
                    && minPrice != null
                    && maxPrice != null) {

                reservations =
                        reservationRepository
                                .findByStatusAndPriceBetween(
                                        status,
                                        minPrice,
                                        maxPrice,
                                        pageable);

            } else if (status != null) {

                reservations =
                        reservationRepository
                                .findByStatus(
                                        status,
                                        pageable);

            } else if (minPrice != null
                    && maxPrice != null) {

                reservations =
                        reservationRepository
                                .findByPriceBetween(
                                        minPrice,
                                        maxPrice,
                                        pageable);

            } else if (minPrice != null) {

                reservations =
                        reservationRepository
                                .findByPriceGreaterThanEqual(
                                        minPrice,
                                        pageable);

            } else if (maxPrice != null) {

                reservations =
                        reservationRepository
                                .findByPriceLessThanEqual(
                                        maxPrice,
                                        pageable);

            } else {

                reservations =
                        reservationRepository
                                .findAll(pageable);
            }

        } else {

            if (status != null
                    && minPrice != null
                    && maxPrice != null) {

                reservations =
                        reservationRepository
                                .findByUserUsernameAndStatusAndPriceBetween(
                                        username,
                                        status,
                                        minPrice,
                                        maxPrice,
                                        pageable);

            } else if (status != null) {

                reservations =
                        reservationRepository
                                .findByUserUsernameAndStatus(
                                        username,
                                        status,
                                        pageable);

            } else if (minPrice != null
                    && maxPrice != null) {

                reservations =
                        reservationRepository
                                .findByUserUsernameAndPriceBetween(
                                        username,
                                        minPrice,
                                        maxPrice,
                                        pageable);

            } else if (minPrice != null) {

                reservations =
                        reservationRepository
                                .findByUserUsernameAndPriceGreaterThanEqual(
                                        username,
                                        minPrice,
                                        pageable);

            } else if (maxPrice != null) {

                reservations =
                        reservationRepository
                                .findByUserUsernameAndPriceLessThanEqual(
                                        username,
                                        maxPrice,
                                        pageable);

            } else {

                reservations =
                        reservationRepository
                                .findByUserUsername(
                                        username,
                                        pageable);
            }
        }

        return reservations.map(this::mapToResponse);
    }

    public ReservationResponse updateReservation(
            Long id,
            ReservationUpdateRequest request) {

        Reservation reservation =
                reservationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reservation not found"));

        /*
         * Allow status-only updates.
         * This is required when an admin wants to change
         * PENDING -> CONFIRMED or PENDING -> CANCELLED
         * without changing the reservation details.
         */
        if (request.getStatus() != null
                && request.getResourceId() == null
                && request.getStartTime() == null
                && request.getEndTime() == null) {

            reservation.setStatus(request.getStatus());
            reservation.setUpdatedAt(LocalDateTime.now());

            Reservation updatedReservation =
                    reservationRepository.save(reservation);

            return mapToResponse(updatedReservation);
        }

        /*
         * If reservation details are being changed,
         * all required fields must be provided.
         */
        if (request.getResourceId() == null
                || request.getStartTime() == null
                || request.getEndTime() == null) {

            throw new IllegalArgumentException(
                    "resourceId, startTime and endTime are required when updating reservation details");
        }

        if (!request.getEndTime()
                .isAfter(request.getStartTime())) {

            throw new IllegalArgumentException(
                    "End time must be after start time");
        }

        Resource resource =
                resourceRepository.findById(
                                request.getResourceId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Resource not found"));

        if (!resource.isAvailable()) {
            throw new IllegalArgumentException(
                    "Resource is not available");
        }

        reservation.setResource(resource);
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());

        reservation.setPrice(resource.getPrice());

        if (request.getStatus() != null) {
            reservation.setStatus(request.getStatus());
        }

        reservation.setUpdatedAt(LocalDateTime.now());

        Reservation updatedReservation =
                reservationRepository.save(reservation);

        return mapToResponse(updatedReservation);
    }

    public void deleteReservation(Long id) {

        Reservation reservation =
                reservationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reservation not found"));

        reservationRepository.delete(reservation);
    }

    private ReservationResponse mapToResponse(
            Reservation reservation) {

        return new ReservationResponse(
                reservation.getId(),
                reservation.getUser().getId(),
                reservation.getUser().getUsername(),
                reservation.getResource().getId(),
                reservation.getResource().getName(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getPrice(),
                reservation.getStatus(),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt()
        );
    }
}