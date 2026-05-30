package com.booking.Booking_Project.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.booking.Booking_Project.model.Booking;
import com.booking.Booking_Project.model.Session;

@Repository
public interface BookingRepository
        extends JpaRepository<Booking, UUID> {

    List<Booking> findByParentId(
            UUID parentId);

    @Query("""
        SELECT s
        FROM Booking b
        JOIN b.offering o
        JOIN Session s
            ON s.offering.id = o.id
        WHERE b.parent.id = :parentId
    """)
    List<Session> findBookedSessionsByParent(
            UUID parentId);

    boolean existsByParentIdAndOfferingId(UUID parentId, UUID offeringId);
}
