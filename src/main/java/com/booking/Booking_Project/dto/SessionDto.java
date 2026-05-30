package com.booking.Booking_Project.dto;

import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionDto {

    private ZonedDateTime startTime;

    private ZonedDateTime endTime;
}