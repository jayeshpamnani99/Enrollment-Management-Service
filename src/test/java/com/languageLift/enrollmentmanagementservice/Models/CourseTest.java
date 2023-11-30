package com.languageLift.enrollmentmanagementservice.Models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CourseTest {

    @Test
    public void testCourseGettersAndSetters() {
        // Arrange
        Course course = new Course();

        // Act
        course.setId(1);
        course.setTitle("Course Title");
        course.setInstructorId(2);
        course.setDescription("Course Description");
        course.setAvgDuration(3);
        course.setDifficulty("Easy");

        // Assert
        assertEquals(1, course.getId());
        assertEquals("Course Title", course.getTitle());
        assertEquals(2, course.getInstructorId());
        assertEquals("Course Description", course.getDescription());
        assertEquals(3, course.getAvgDuration());
        assertEquals("Easy", course.getDifficulty());
    }

    @Test
    public void testInstructorDetailsGetterAndSetter() {
        // Arrange
        Course course = new Course();
        HashMap<String, String> instructorDetails = new HashMap<>();
        instructorDetails.put("name", "John Doe");
        instructorDetails.put("email", "john.doe@example.com");

        // Act
        course.setInstructorDetails(instructorDetails);

        // Assert
        assertEquals(instructorDetails, course.getInstructorDetails());
    }

    @Test
    public void testToString() {
        // Arrange
        Course course = new Course();
        course.setId(1);
        course.setTitle("Course Title");
        course.setInstructorId(2);
        course.setDescription("Course Description");
        course.setAvgDuration(3);
        course.setDifficulty("Easy");

        // Act
        String toStringResult = course.toString();

        // Assert
        assertTrue(toStringResult.contains("id=1"));
        assertTrue(toStringResult.contains("title='Course Title'"));
        assertTrue(toStringResult.contains("instructorId=2"));
        assertTrue(toStringResult.contains("description='Course Description'"));
        assertTrue(toStringResult.contains("avgDuration=3"));
        assertTrue(toStringResult.contains("difficulty='Easy'"));
    }
}

