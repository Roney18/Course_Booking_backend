package com.booking.Booking_Project.dto;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AvailableOfferingResponse {

    private UUID offeringId;

    private String offeringName;

    private String courseName;

    private List<SessionDto> sessions;
}
