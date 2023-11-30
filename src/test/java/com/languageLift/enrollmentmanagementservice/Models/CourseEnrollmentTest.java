package com.languageLift.enrollmentmanagementservice.Models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CourseEnrollmentTest {

    @Test
    public void testCourseEnrollmentGettersAndSetters() {
        // Arrange
        CourseEnrollment courseEnrollment = new CourseEnrollment();

        // Act
        courseEnrollment.setId(1);
        courseEnrollment.setCourseId(2);
        courseEnrollment.setStudentId(3);
        courseEnrollment.setStatus("Enrolled");
        Date enrollmentDate = new Date(System.currentTimeMillis());
        courseEnrollment.setEnrollmentDate(enrollmentDate);
        Date completionDate = new Date(System.currentTimeMillis());
        courseEnrollment.setCompletionDate(completionDate);

        // Assert
        assertEquals(1, courseEnrollment.getId());
        assertEquals(2, courseEnrollment.getCourseId());
        assertEquals(3, courseEnrollment.getStudentId());
        assertEquals("Enrolled", courseEnrollment.getStatus());
        assertEquals(enrollmentDate, courseEnrollment.getEnrollmentDate());
        assertEquals(completionDate, courseEnrollment.getCompletionDate());
    }

    @Test
    public void testToString() {
        // Arrange
        CourseEnrollment courseEnrollment = new CourseEnrollment();
        courseEnrollment.setId(1);
        courseEnrollment.setCourseId(2);
        courseEnrollment.setStudentId(3);
        courseEnrollment.setStatus("Enrolled");
        Date enrollmentDate = new Date(System.currentTimeMillis());
        courseEnrollment.setEnrollmentDate(enrollmentDate);
        Date completionDate = new Date(System.currentTimeMillis());
        courseEnrollment.setCompletionDate(completionDate);

        // Act
        String toStringResult = courseEnrollment.toString();

        // Assert
        assertFalse(toStringResult.contains("courseId"));
    }
}

