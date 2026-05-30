package com.booking.Booking_Project.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.booking.Booking_Project.dto.AvailableOfferingResponse;
import com.booking.Booking_Project.dto.CreateParentRequest;
import com.booking.Booking_Project.dto.ParentResponse;
import com.booking.Booking_Project.dto.SessionDto;
import com.booking.Booking_Project.exception.ResourceNotFoundException;
import com.booking.Booking_Project.model.Parent;
import com.booking.Booking_Project.repository.OfferingRepository;
import com.booking.Booking_Project.repository.ParentRepository;
import com.booking.Booking_Project.repository.SessionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParentService {

    private final ParentRepository parentRepository;
    private final OfferingRepository offeringRepository;
    private final SessionRepository sessionRepository;



    public ParentResponse createParent(
            CreateParentRequest request) {

        Parent parent = new Parent();

        parent.setName(request.getName());
        parent.setTimezone(
                request.getTimezone());

        Parent saved =
                parentRepository.save(parent);

        return ParentResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .timezone(saved.getTimezone())
                .build();
    }


    public List<AvailableOfferingResponse>
    getAvailableOfferings(UUID parentId) {

        Parent parent =
                parentRepository.findById(parentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Parent not found"));

        ZoneId parentZone =
                ZoneId.of(parent.getTimezone());

        return offeringRepository.findAll()
                .stream()
                .map(offering -> {

                    List<SessionDto> sessions =
                            sessionRepository
                                    .findByOfferingId(
                                            offering.getId())
                                    .stream()
                                    .map(session -> {

                                        ZonedDateTime start =
                                                session
                                                        .getStartTime()
                                                        .atZone(parentZone);

                                        ZonedDateTime end =
                                                session
                                                        .getEndTime()
                                                        .atZone(parentZone);

                                        return SessionDto
                                                .builder()
                                                .startTime(start)
                                                .endTime(end)
                                                .build();
                                    })
                                    .toList();

                    return AvailableOfferingResponse
                            .builder()
                            .offeringId(
                                    offering.getId())
                            .offeringName(
                                    offering.getName())
                            .courseName(
                                    offering.getCourse()
                                            .getName())
                            .sessions(sessions)
                            .build();
                })
                .toList();
    }
}
