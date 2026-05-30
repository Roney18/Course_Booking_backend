package com.booking.Booking_Project.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest {

    @NotNull(message = "Parent ID is required")
    private UUID parentId;

    @NotNull(message = "Offering ID is required")
    private UUID offeringId;
}