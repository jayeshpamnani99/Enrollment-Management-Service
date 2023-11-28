package com.languageLift.enrollmentmanagementservice.Services;

import com.languageLift.enrollmentmanagementservice.Dao.CourseDao;
import com.languageLift.enrollmentmanagementservice.Dao.CourseEnrollmentDao;
import com.languageLift.enrollmentmanagementservice.Models.Course;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EnrollmentServiceTest
{
    @Mock

    private CourseEnrollmentDao courseEnrollmentDao;
    @Mock
    private CourseDao courseDao;
    @InjectMocks
    private EnrollmentService enrollmentService;


    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testCheckIfUserAlreadyRegisteredInCourse() throws Exception {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void testEnroll() throws Exception {
        throw new RuntimeException("Test not implemented");
    }

    @Test
    public void testGetAllCourses() throws Exception {
        // Act
        List<Course> courses = enrollmentService.getAllCourses("validToken");

        // Assert
        assertNotNull(courses);
        assertEquals(courses.size(), 3);
    }

    @Test
    public void testCheckIfUserAlreadyRegisteredInCourse2() {

        // Act
        boolean isRegistered = enrollmentService.checkIfUserAlreadyRegisteredInCourse(1, 1);

        // Assert
        assertTrue(isRegistered);
    }

    @Test
    public void getEnrolledCoursesByStuId() throws Exception {

        // Act
        JSONObject enrolledCourses = enrollmentService.getEnrolledCoursesByStuId("validToken");

        // Assert
        assertNotNull(enrolledCourses);
        assertEquals(enrolledCourses.getJSONArray("courseDetails").length(), 1);
    }

    @Test
    public void getUserDetailsFromUserId() throws JSONException {

        // Act
        JSONObject userDetails = enrollmentService.getUserDetailsFromUserId(1);

        // Assert
        assertNotNull(userDetails);
        assertEquals(userDetails.getString("name"), "John Doe");
    }

    @Test
    public void testEnroll2() throws Exception {

        // Act
        int enrollmentId = enrollmentService.enroll("validToken", 2);

        // Assert
        assertEquals(enrollmentId, 2);
    }




}
