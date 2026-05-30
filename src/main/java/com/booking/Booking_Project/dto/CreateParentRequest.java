package com.booking.Booking_Project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateParentRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Timezone is required")
    private String timezone;
}
