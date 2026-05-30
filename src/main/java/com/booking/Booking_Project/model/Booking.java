package com.booking.Booking_Project.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Data
@Table(
    name = "bookings",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"parent_id", "offering_id"}
        )
    }
)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

   @ManyToOne
@JoinColumn(name = "parent_id")
private Parent parent;

@ManyToOne
@JoinColumn(name = "offering_id")
private Offering offering;

    private Instant bookedAt;
}
