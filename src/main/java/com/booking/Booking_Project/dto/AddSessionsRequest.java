package com.booking.Booking_Project.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AddSessionsRequest {
    @NotEmpty(message = "At least one session is required")
    @Valid
    private List<SessionRequest> sessions;
}
