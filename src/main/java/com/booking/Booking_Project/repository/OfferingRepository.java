package com.booking.Booking_Project.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.booking.Booking_Project.model.Offering;

@Repository
public interface OfferingRepository
        extends JpaRepository<Offering, UUID> {

     @EntityGraph(
            attributePaths = {
                    "course"
            }
    )
    List<Offering> findByTeacherId(
            UUID teacherId);
}
