package com.booking.Booking_Project.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.booking.Booking_Project.dto.BookingRequest;
import com.booking.Booking_Project.dto.BookingResponse;
import com.booking.Booking_Project.exception.BadRequestException;
import com.booking.Booking_Project.exception.ConflictException;
import com.booking.Booking_Project.exception.ResourceNotFoundException;
import com.booking.Booking_Project.model.Booking;
import com.booking.Booking_Project.model.Offering;
import com.booking.Booking_Project.model.Parent;
import com.booking.Booking_Project.model.Session;
import com.booking.Booking_Project.repository.BookingRepository;
import com.booking.Booking_Project.repository.OfferingRepository;
import com.booking.Booking_Project.repository.ParentRepository;
import com.booking.Booking_Project.repository.SessionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ParentRepository parentRepository;
    private final OfferingRepository offeringRepository;
    private final SessionRepository sessionRepository;

    public BookingResponse bookOffering(
            BookingRequest request) {

        Parent parent =
                parentRepository
                        .findByIdForUpdate(
                                request.getParentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Parent not found"));

        Offering offering =
                offeringRepository
                        .findById(
                                request.getOfferingId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Offering not found"));

        // Prevent duplicate booking
        if (bookingRepository
                .existsByParentIdAndOfferingId(
                        request.getParentId(),
                        request.getOfferingId())) {

            throw new ConflictException(
                    "Offering already booked");
        }

        // Offering must contain sessions
        List<Session> offeringSessions =
                sessionRepository.findByOfferingId(
                        offering.getId());

        if (offeringSessions.isEmpty()) {

            throw new BadRequestException(
                    "Offering has no sessions");
        }

        // Validate overlapping sessions
        validateConflicts(
                parent.getId(),
                offering.getId());

        Booking booking = new Booking();

        booking.setParent(parent);
        booking.setOffering(offering);
        booking.setBookedAt(
                Instant.now());

        Booking saved =
                bookingRepository.save(booking);

        return BookingResponse.builder()
                .bookingId(saved.getId())
                .offeringId(offering.getId())
                .offeringName(offering.getName())
                .bookedAt(saved.getBookedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public List<BookingResponse>
    getBookings(UUID parentId) {

        return bookingRepository
                .findByParentId(parentId)
                .stream()
                .map(booking ->
                        BookingResponse.builder()
                                .bookingId(
                                        booking.getId())
                                .offeringId(
                                        booking.getOffering()
                                                .getId())
                                .offeringName(
                                        booking.getOffering()
                                                .getName())
                                .bookedAt(
                                        booking.getBookedAt())
                                .build())
                .toList();
    }

    private void validateConflicts(
            UUID parentId,
            UUID offeringId) {

        List<Session> bookedSessions =
                bookingRepository
                        .findBookedSessionsByParent(
                                parentId);

        List<Session> incomingSessions =
                sessionRepository
                        .findByOfferingId(
                                offeringId);

        for (Session booked : bookedSessions) {

            for (Session incoming :
                    incomingSessions) {

                if (isOverlapping(
                        booked.getStartTime(),
                        booked.getEndTime(),
                        incoming.getStartTime(),
                        incoming.getEndTime())) {

                    throw new ConflictException(
                            "Schedule overlap detected");
                }
            }
        }
    }

    private boolean isOverlapping(
            Instant start1,
            Instant end1,
            Instant start2,
            Instant end2) {

        return start1.isBefore(end2)
                && end1.isAfter(start2);
    }
}