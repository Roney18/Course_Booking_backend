package com.booking.Booking_Project.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OfferingResponse {

    private UUID id;

    private String courseName;

    private String offeringName;

    private UUID teacherId;

    private String teacherTimezone;
}
