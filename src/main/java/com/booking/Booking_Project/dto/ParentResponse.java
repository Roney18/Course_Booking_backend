package com.booking.Booking_Project.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParentResponse {

    private UUID id;
    private String name;
    private String timezone;
}