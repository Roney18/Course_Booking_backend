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

import com.booking.Booking_Project.dto.AddSessionsRequest;
import com.booking.Booking_Project.dto.CreateOfferingRequest;
import com.booking.Booking_Project.dto.OfferingResponse;
import com.booking.Booking_Project.service.OfferingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final OfferingService offeringService;

    @PostMapping("/offerings")
    public ResponseEntity<OfferingResponse> createOffering(
           @Valid @RequestBody CreateOfferingRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(offeringService.createOffering(request));
    }

    @PostMapping("/offerings/{offeringId}/sessions")
    public ResponseEntity<Void> addSessions(
            @PathVariable UUID offeringId,
            @Valid @RequestBody AddSessionsRequest request) {

        offeringService.addSessions(offeringId, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{teacherId}/offerings")
    public ResponseEntity<List<OfferingResponse>> getTeacherOfferings(
            @PathVariable UUID teacherId) {

        return ResponseEntity.ok(
                offeringService.getTeacherOfferings(teacherId)
        );
    }
}
