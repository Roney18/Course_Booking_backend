package com.booking.Booking_Project.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.booking.Booking_Project.model.Session;

@Repository
public interface SessionRepository
        extends JpaRepository<Session, UUID> {

    @Query("""
        SELECT s
        FROM Session s
        WHERE s.offering.id = :offeringId
        ORDER BY s.startTime
    """)
    List<Session> findByOfferingId(
            UUID offeringId);
}
