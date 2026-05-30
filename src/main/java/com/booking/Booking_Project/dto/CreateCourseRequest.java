package com.booking.Booking_Project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCourseRequest {
    @NotBlank(message = "Course name is required")
    private String name;

    @NotBlank(message = "Course description is required")
    private String description;
}
