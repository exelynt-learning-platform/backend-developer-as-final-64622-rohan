package com.rohan.booking.repository;

import com.rohan.booking.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReservationRepository
        extends JpaRepository<Reservation, Long>,
        JpaSpecificationExecutor<Reservation> {

    @Override
    @EntityGraph(attributePaths = {"user", "resource"})
    Page<Reservation> findAll(
            Specification<Reservation> specification,
            Pageable pageable);
}