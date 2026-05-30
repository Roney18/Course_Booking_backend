package com.booking.Booking_Project.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.booking.Booking_Project.model.Parent;

import jakarta.persistence.LockModeType;

@Repository
public interface ParentRepository
        extends JpaRepository<Parent, UUID> {
            @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT p
        FROM Parent p
        WHERE p.id = :parentId
    """)
    Optional<Parent> findByIdForUpdate(
            UUID parentId);
}
