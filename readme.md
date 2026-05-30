# Global Class Offering Booking System

## Overview

This project is a backend system for managing and booking global online class offerings.

Teachers can create course offerings and schedule sessions in their local timezone. Parents can browse available offerings, view sessions converted to their own timezone, and book offerings for their children.

The system includes:

* Course management
* Parent management
* Teacher offerings
* Session scheduling
* Booking management
* Timezone conversion
* Schedule conflict detection
* Concurrency handling
* Swagger API documentation

---

## Tech Stack

* Java 21
* Spring Boot 3
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* Lombok
* Swagger / OpenAPI

---

## Architecture

### Entities

#### Course

Represents a course that can have multiple offerings.

#### Parent

Represents a parent who can browse offerings and create bookings.

#### Offering

Represents a teacher's offering for a specific course.

#### Session

Represents scheduled class sessions belonging to an offering.

#### Booking

Represents a parent's booking for an offering.

---

## Features

### Course Management

* Create course
* View all courses

### Parent Management

* Create parent
* View available offerings
* View bookings

### Teacher Offering Management

* Create offering
* Add sessions to offering
* View teacher offerings

### Booking Management

* Book offering
* View parent bookings

### Timezone Conversion

Teacher sessions are stored in UTC.

When a parent views offerings, session times are automatically converted into the parent's timezone.

### Conflict Detection

Before creating a booking, the system checks whether any session in the requested offering overlaps with sessions from already booked offerings.

If an overlap is detected, the booking is rejected.

### Concurrency Handling

Booking operations use pessimistic locking on the Parent entity.

This prevents race conditions during simultaneous booking requests.

Implementation:

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
```

---

## API Endpoints

### Course APIs

| Method | Endpoint     |
| ------ | ------------ |
| POST   | /api/courses |
| GET    | /api/courses |

---

### Parent APIs

| Method | Endpoint                          |
| ------ | --------------------------------- |
| POST   | /api/parents                      |
| GET    | /api/parents/{parentId}/offerings |
| POST   | /api/parents/bookings             |
| GET    | /api/parents/{parentId}/bookings  |

---

### Teacher APIs

| Method | Endpoint                                      |
| ------ | --------------------------------------------- |
| POST   | /api/teachers/offerings                       |
| POST   | /api/teachers/offerings/{offeringId}/sessions |
| GET    | /api/teachers/{teacherId}/offerings           |

---

## Database Design

### Course

```text
id
name
description
```

### Parent

```text
id
name
timezone
```

### Offering

```text
id
course_id
teacher_id
name
teacher_timezone
```

### Session

```text
id
offering_id
start_time
end_time
```

### Booking

```text
id
parent_id
offering_id
booked_at
```

---

## Assumptions

* Teacher information is managed externally.
* Only the teacher UUID is stored in the Offering entity.
* Parent timezone is provided during parent creation.
* Session times are stored internally in UTC.
* A parent cannot book the same offering twice.
* A parent cannot book offerings with overlapping session schedules.

---

## Validation Rules

### Course

* Name is required
* Description is required

### Parent

* Name is required
* Timezone is required

### Offering

* Course must exist
* Teacher timezone must be valid

### Session

* Start time is required
* End time is required
* End time must be after start time

### Booking

* Parent must exist
* Offering must exist
* Duplicate bookings are not allowed
* Schedule conflicts are not allowed

---

## Running the Application

### Prerequisites

* Java 21
* Maven
* PostgreSQL

### Run Application

```bash
mvn clean install
mvn spring-boot:run
```

Application runs on:

```text
http://localhost:8081
```

---

## Swagger Documentation

Swagger UI:

```text
http://localhost:8081/swagger-ui/index.html
```
---

## API Examples

### 1. Create Course

**Request**

```http
POST /api/courses
```

```json
{
  "name": "Minecraft Coding",
  "description": "Learn Minecraft Modding"
}
```

**Response**

```json
{
  "id": "d9ace56e-c5c3-4847-895f-01fdccea9ced",
  "name": "Minecraft Coding",
  "description": "Learn Minecraft Modding"
}
```

---

### 2. Get All Courses

**Request**

```http
GET /api/courses
```

**Response**

```json
[
  {
    "id": "d9ace56e-c5c3-4847-895f-01fdccea9ced",
    "name": "Minecraft Coding",
    "description": "Learn Minecraft Modding"
  }
]
```

---

### 3. Create Parent

**Request**

```http
POST /api/parents
```

```json
{
  "name": "Roney",
  "timezone": "Asia/Kolkata"
}
```

**Response**

```json
{
  "id": "b32cdbd8-6ebf-4e45-b67a-61c9f873d9d4",
  "name": "Roney",
  "timezone": "Asia/Kolkata"
}
```

---

### 4. Create Offering

**Request**

```http
POST /api/teachers/offerings
```

```json
{
  "courseId": "d9ace56e-c5c3-4847-895f-01fdccea9ced",
  "teacherId": "377fa110-8655-43a5-a93a-0af9a6330833",
  "name": "Saturday Batch",
  "teacherTimezone": "Asia/Kolkata"
}
```

**Response**

```json
{
  "id": "0372fff5-f8ed-487e-8e57-0f88561e6d8a",
  "courseName": "Minecraft Coding",
  "offeringName": "Saturday Batch",
  "teacherId": "377fa110-8655-43a5-a93a-0af9a6330833",
  "teacherTimezone": "Asia/Kolkata"
}
```

---

### 5. Add Sessions

**Request**

```http
POST /api/teachers/offerings/{offeringId}/sessions
```

```json
{
  "sessions": [
    {
      "startTime": "2026-06-15T18:00:00",
      "endTime": "2026-06-15T19:00:00"
    },
    {
      "startTime": "2026-06-22T18:00:00",
      "endTime": "2026-06-22T19:00:00"
    }
  ]
}
```

**Response**

```http
201 Created
```

---

### 6. Get Teacher Offerings

**Request**

```http
GET /api/teachers/{teacherId}/offerings
```

**Response**

```json
[
  {
    "id": "0372fff5-f8ed-487e-8e57-0f88561e6d8a",
    "courseName": "Minecraft Coding",
    "offeringName": "Saturday Batch",
    "teacherId": "377fa110-8655-43a5-a93a-0af9a6330833",
    "teacherTimezone": "Asia/Kolkata"
  }
]
```

---

### 7. Get Available Offerings For Parent

**Request**

```http
GET /api/parents/{parentId}/offerings
```

**Response**

```json
[
  {
    "offeringId": "0372fff5-f8ed-487e-8e57-0f88561e6d8a",
    "offeringName": "Saturday Batch",
    "courseName": "Minecraft Coding",
    "sessions": [
      {
        "startTime": "2026-06-15T18:00:00+05:30",
        "endTime": "2026-06-15T19:00:00+05:30"
      }
    ]
  }
]
```

---

### 8. Create Booking

**Request**

```http
POST /api/parents/bookings
```

```json
{
  "parentId": "b32cdbd8-6ebf-4e45-b67a-61c9f873d9d4",
  "offeringId": "0372fff5-f8ed-487e-8e57-0f88561e6d8a"
}
```

**Response**

```json
{
  "bookingId": "8a77f7b3-8c95-4dd4-9e4c-8f8f0f7c1f88",
  "offeringId": "0372fff5-f8ed-487e-8e57-0f88561e6d8a",
  "offeringName": "Saturday Batch",
  "bookedAt": "2026-05-30T07:30:15Z"
}
```

---

### 9. Get Parent Bookings

**Request**

```http
GET /api/parents/{parentId}/bookings
```

**Response**

```json
[
  {
    "bookingId": "8a77f7b3-8c95-4dd4-9e4c-8f8f0f7c1f88",
    "offeringId": "0372fff5-f8ed-487e-8e57-0f88561e6d8a",
    "offeringName": "Saturday Batch",
    "bookedAt": "2026-05-30T07:30:15Z"
  }
]
```


## Demo Recording

Demo Video:

```text
https://drive.google.com/file/d/1bRocwWAkOHcuhAEyXu_dONl_awh_b4tZ/view?usp=sharing
```

---

## Future Improvements

* Spring Security
* JWT Authentication
* Teacher Management Module
* Role-Based Access Control
* Flyway Database Migration
* Docker Support
* Automated Testing
* CI/CD Pipeline

---


Tapabrata Dhara

Backend Engineering Assignment
