package com.booking.Booking_Project.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOfferingRequest {

    @NotNull(message = "Course ID is required")
    private UUID courseId;

    @NotNull(message = "Teacher ID is required")
    private UUID teacherId;

    @NotBlank(message = "Offering name is required")
    private String name;

    @NotBlank(message = "Teacher timezone is required")
    private String teacherTimezone;
}