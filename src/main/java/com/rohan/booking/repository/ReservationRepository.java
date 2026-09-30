package com.rohan.booking.repository;

import com.rohan.booking.entity.Reservation;
import com.rohan.booking.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {

    @EntityGraph(attributePaths = {"user", "resource"})
    Page<Reservation> findByUserUsername(
            String username,
            Pageable pageable);

    @EntityGraph(attributePaths = {"user", "resource"})
    Page<Reservation> findByStatus(
            ReservationStatus status,
            Pageable pageable);

    @EntityGraph(attributePaths = {"user", "resource"})
    Page<Reservation> findByPriceGreaterThanEqual(
            BigDecimal minPrice,
            Pageable pageable);

    @EntityGraph(attributePaths = {"user", "resource"})
    Page<Reservation> findByPriceLessThanEqual(
            BigDecimal maxPrice,
            Pageable pageable);

    @EntityGraph(attributePaths = {"user", "resource"})
    Page<Reservation> findByPriceBetween(
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable);

    @EntityGraph(attributePaths = {"user", "resource"})
    Page<Reservation> findByStatusAndPriceBetween(
            ReservationStatus status,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable);

    @EntityGraph(attributePaths = {"user", "resource"})
    Page<Reservation> findByUserUsernameAndStatus(
            String username,
            ReservationStatus status,
            Pageable pageable);

    @EntityGraph(attributePaths = {"user", "resource"})
    Page<Reservation> findByUserUsernameAndPriceGreaterThanEqual(
            String username,
            BigDecimal minPrice,
            Pageable pageable);

    @EntityGraph(attributePaths = {"user", "resource"})
    Page<Reservation> findByUserUsernameAndPriceLessThanEqual(
            String username,
            BigDecimal maxPrice,
            Pageable pageable);

    @EntityGraph(attributePaths = {"user", "resource"})
    Page<Reservation> findByUserUsernameAndPriceBetween(
            String username,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable);

    @EntityGraph(attributePaths = {"user", "resource"})
    Page<Reservation> findByUserUsernameAndStatusAndPriceBetween(
            String username,
            ReservationStatus status,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"user", "resource"})
    Page<Reservation> findAll(Pageable pageable);
}