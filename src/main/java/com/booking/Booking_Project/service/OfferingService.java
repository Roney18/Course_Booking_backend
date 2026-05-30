package com.booking.Booking_Project.service;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.booking.Booking_Project.dto.AddSessionsRequest;
import com.booking.Booking_Project.dto.CreateOfferingRequest;
import com.booking.Booking_Project.dto.OfferingResponse;
import com.booking.Booking_Project.exception.BadRequestException;
import com.booking.Booking_Project.exception.ResourceNotFoundException;
import com.booking.Booking_Project.model.Course;
import com.booking.Booking_Project.model.Offering;
import com.booking.Booking_Project.model.Session;
import com.booking.Booking_Project.repository.CourseRepository;
import com.booking.Booking_Project.repository.OfferingRepository;
import com.booking.Booking_Project.repository.SessionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OfferingService {

    private final OfferingRepository offeringRepository;
    private final CourseRepository courseRepository;
    private final SessionRepository sessionRepository;

    public OfferingResponse createOffering(
            CreateOfferingRequest request) {

        validateTimezone(request.getTeacherTimezone());

        Course course = courseRepository.findById(
                        request.getCourseId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found"));

        Offering offering = new Offering();

        offering.setCourse(course);
        offering.setTeacherId(request.getTeacherId());
        offering.setName(request.getName());
        offering.setTeacherTimezone(
                request.getTeacherTimezone());

        Offering saved =
                offeringRepository.save(offering);

        return map(saved);
    }

    public void addSessions(
            UUID offeringId,
            AddSessionsRequest request) {

        Offering offering =
                offeringRepository.findById(offeringId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Offering not found"));

        ZoneId teacherZone =
                ZoneId.of(
                        offering.getTeacherTimezone());

        List<Session> sessions =
                request.getSessions()
                        .stream()
                        .map(dto -> {

                            if (!dto.getEndTime()
                                    .isAfter(dto.getStartTime())) {

                                throw new BadRequestException(
                                        "End time must be after start time");
                            }

                            Session session =
                                    new Session();

                            session.setOffering(offering);

                            session.setStartTime(
                                    dto.getStartTime()
                                            .atZone(teacherZone)
                                            .toInstant());

                            session.setEndTime(
                                    dto.getEndTime()
                                            .atZone(teacherZone)
                                            .toInstant());

                            return session;
                        })
                        .toList();

        sessionRepository.saveAll(sessions);
    }

    @Transactional(readOnly = true)
    public List<OfferingResponse>
    getTeacherOfferings(UUID teacherId) {

        return offeringRepository
                .findByTeacherId(teacherId)
                .stream()
                .map(this::map)
                .toList();
    }

    private void validateTimezone(
            String timezone) {

        try {

            ZoneId.of(timezone);

        } catch (DateTimeException ex) {

            throw new BadRequestException(
                    "Invalid timezone");
        }
    }

    private OfferingResponse map(
            Offering offering) {

        return OfferingResponse.builder()
                .id(offering.getId())
                .courseName(
                        offering.getCourse().getName())
                .offeringName(
                        offering.getName())
                .teacherId(
                        offering.getTeacherId())
                .teacherTimezone(
                        offering.getTeacherTimezone())
                .build();
    }
}