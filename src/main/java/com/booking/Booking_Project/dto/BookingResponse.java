package com.booking.Booking_Project.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingResponse {

    private UUID bookingId;

    private UUID offeringId;

    private String offeringName;

    private Instant bookedAt;
}
