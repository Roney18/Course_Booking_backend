package com.booking.Booking_Project.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.Booking_Project.dto.AvailableOfferingResponse;
import com.booking.Booking_Project.dto.BookingRequest;
import com.booking.Booking_Project.dto.BookingResponse;
import com.booking.Booking_Project.dto.CreateParentRequest;
import com.booking.Booking_Project.dto.ParentResponse;
import com.booking.Booking_Project.service.BookingService;
import com.booking.Booking_Project.service.ParentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/parents")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;
    private final BookingService bookingService;


        @PostMapping
        public ResponseEntity<ParentResponse>
        createParent(
            @Valid
            @RequestBody
            CreateParentRequest request) {

             return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        parentService
                                .createParent(
                                        request));
        }

    @GetMapping("/{parentId}/offerings")
    public ResponseEntity<List<AvailableOfferingResponse>> getAvailableOfferings(
            @PathVariable UUID parentId) {

        return ResponseEntity.ok(
                parentService.getAvailableOfferings(parentId)
        );
    }

    @PostMapping("/bookings")
    public ResponseEntity<BookingResponse> bookOffering(
            @Valid @RequestBody BookingRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        bookingService.bookOffering(request)
                );
    }

    @GetMapping("/{parentId}/bookings")
    public ResponseEntity<List<BookingResponse>> getBookings(
            @PathVariable UUID parentId) {

        return ResponseEntity.ok(
                bookingService.getBookings(parentId)
        );
    }
}
