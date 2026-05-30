package com.booking.Booking_Project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.Booking_Project.dto.CourseResponse;
import com.booking.Booking_Project.dto.CreateCourseRequest;
import com.booking.Booking_Project.model.Course;
import com.booking.Booking_Project.repository.CourseRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseResponse createCourse(
            CreateCourseRequest request) {

        Course course = new Course();

        course.setName(request.getName());
        course.setDescription(request.getDescription());

        Course saved = courseRepository.save(course);

        return CourseResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .description(saved.getDescription())
                .build();
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {

        return courseRepository.findAll()
                .stream()
                .map(course -> CourseResponse.builder()
                        .id(course.getId())
                        .name(course.getName())
                        .description(course.getDescription())
                        .build())
                .toList();
    }
}
