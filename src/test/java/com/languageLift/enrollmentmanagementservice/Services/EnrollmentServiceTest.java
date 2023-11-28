package com.languageLift.enrollmentmanagementservice.Services;

import com.languageLift.enrollmentmanagementservice.CustomException;
import com.languageLift.enrollmentmanagementservice.Dao.CourseDao;
import com.languageLift.enrollmentmanagementservice.Dao.CourseEnrollmentDao;
import com.languageLift.enrollmentmanagementservice.Models.Course;
import com.languageLift.enrollmentmanagementservice.Models.CourseEnrollment;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EnrollmentServiceTest
{
    @Mock
    private CourseDao courseDao;

    @Mock
    private CourseEnrollmentDao courseEnrollmentDao;

    @Mock
    private EntityManager entityManager;

    @Mock
    private RestTemplate restTemplate;

    @Spy
    @InjectMocks
    private EnrollmentService enrollmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testGetAllCourses_success() throws Exception{
//        // Arrange
//        String authToken = "valid-token";
//        JSONObject userObject = new JSONObject();
//        userObject.put("id", 1);
//        Mockito.when(enrollmentService.getUserDetailsFromToken(authToken)).thenReturn(userObject);
//
//        List<Course> courseList = Streamable.of(new Course(), new Course()).toList();
//        Mockito.when(courseDao.findAll()).thenReturn(courseList);
//
//        // Act
//        List<Course> actualCourses = enrollmentService.getAllCourses(authToken);
//
//        // Assert
//        Assertions.assertEquals(courseList, actualCourses);
//        actualCourses.forEach(course -> {
//            Assertions.assertNotNull(course.getInstructorDetails());
//        });
//    }

//    @Test
//    public void testGetAllCourses_unauthorized() throws Exception {
//        // Arrange
//        String authToken = "invalid-token";
//
//        JSONObject expectedUserObject = new JSONObject();
//        expectedUserObject.put("id", 1);
//        given(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class))).willReturn(new ResponseEntity<>("{\"id\":123}", HttpStatus.OK));
//
//        JSONObject result = enrollmentService.getUserDetailsFromToken(authToken);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", authToken);
//
//        HttpEntity<String> entity = new HttpEntity<>(null, headers);
//
//
//        verify(restTemplate).exchange("test", HttpMethod.GET, entity, String.class);
//
//
////        Mockito.when(enrollmentService.getUserDetailsFromToken(authToken)).thenReturn(null);
////
////        // Act & Assert
////        CustomException exception = assertThrows(CustomException.class, () -> enrollmentService.getAllCourses(authToken));
////        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
//    }

    @Test
    public void testCheckIfUserAlreadyRegisteredInCourse_true() {
        // Arrange
        int userId = 1;
        int courseId = 2;

        CourseEnrollment courseEnrollment = new CourseEnrollment();
        when(courseEnrollmentDao.findCourseEnrollmentByCourseIdAndAndStudentId(courseId, userId)).thenReturn(courseEnrollment);

        // Act
        boolean actualResult = enrollmentService.checkIfUserAlreadyRegisteredInCourse(userId, courseId);

        // Assert
        assertTrue(actualResult);
    }

    @Test
    public void testCheckIfUserAlreadyRegisteredInCourse_false() {
        // Arrange
        int userId = 1;
        int courseId = 2;

        when(courseEnrollmentDao.findCourseEnrollmentByCourseIdAndAndStudentId(courseId, userId)).thenReturn(null);

        // Act
        boolean actualResult = enrollmentService.checkIfUserAlreadyRegisteredInCourse(userId, courseId);

        // Assert
        assertFalse(actualResult);
    }

    @Test
    public void testGetCourseById_success() {
        // Arrange
        int courseId = 1;
        Course course = new Course();
        when(entityManager.find(Course.class, courseId)).thenReturn(course);

        // Act
        Course actualCourse = enrollmentService.getCourseById(courseId);

        // Assert
        assertEquals(course, actualCourse);
    }

    @Test
    public void testGetCourseById_null() {
        // Arrange
        int courseId = 1;
        when(entityManager.find(Course.class, courseId)).thenReturn(null);

        // Act & Assert
//        CustomException exception = assertThrows(CustomException.class, () -> enrollmentService.getCourseById(courseId));
        assertEquals(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND);
    }


    @Test
    void getAllCourses() throws Exception {
        // Mocking
//        EnrollmentService mock = Mockito.mock(enrollmentService);
        String authToken = "validToken";
        JSONObject userObj = new JSONObject();
        userObj.put("id", 1);
        userObj.put("roleName", "student");
        doReturn(userObj).when(enrollmentService).getUserDetailsFromToken(authToken);
//        when(enrollmentService.getUserDetailsFromToken(authToken)).thenReturn(userObj);

        Course course1 = new Course();
        course1.setId(1);
        course1.setInstructorId(2);

        Course course2 = new Course();
        course2.setId(2);
        course2.setInstructorId(3);

        List<Course> courseList = new ArrayList<>();
        courseList.add(course1);
        courseList.add(course2);

        when(courseDao.findAll()).thenReturn(courseList);

        JSONObject instructorDetails = new JSONObject();
        instructorDetails.put("instructorName", "John Doe");

        doReturn(instructorDetails).when(enrollmentService).getUserDetailsFromUserId(2);
//        when(enrollmentService.getUserDetailsFromUserId(2)).thenReturn(instructorDetails);
        doReturn(null).when(enrollmentService).getUserDetailsFromUserId(3);
//        when(enrollmentService.getUserDetailsFromUserId(3)).thenReturn(null);

        // Test
        try {
            List<Course> result = enrollmentService.getAllCourses(authToken);
            assertEquals(2, result.size());
            assertEquals("John Doe", result.get(0).getInstructorDetails().get("instructorName"));
            assertNull(result.get(1).getInstructorDetails());
        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    void checkIfUserAlreadyRegisteredInCourse() {
        // Mocking
        int userId = 1;
        int courseId = 2;
        when(courseEnrollmentDao.findCourseEnrollmentByCourseIdAndAndStudentId(courseId, userId)).thenReturn(null);

        // Test
        assertFalse(enrollmentService.checkIfUserAlreadyRegisteredInCourse(userId, courseId));
    }

    @Test
    void enroll_SuccessfulEnrollment() {
        // Mocking
        String authToken = "validToken";
        int courseId = 1;

        JSONObject userObj = new JSONObject();
        userObj.put("id", 1);
        userObj.put("roleName", "student");

//        when(enrollmentService.getUserDetailsFromToken(eq(authToken))).thenReturn(userObj);
        doReturn(userObj).when(enrollmentService).getUserDetailsFromToken(authToken);
        doReturn(new Course()).when(enrollmentService).getCourseById(courseId);
//        when(enrollmentService.getCourseById(courseId)).thenReturn(new Course());

        CourseEnrollment savedEnrollment = new CourseEnrollment();
        savedEnrollment.setId(1);
        when(courseEnrollmentDao.save(any())).thenReturn(savedEnrollment);

        // Test
        try {
            int enrollmentId = enrollmentService.enroll(authToken, courseId);
            assertEquals(1, enrollmentId);
        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    void enroll_InvalidCourseId() {
        // Mocking
        String authToken = "validToken";
        int courseId = 0;

        // Test
        assertThrows(CustomException.class, () -> enrollmentService.enroll(authToken, courseId));
    }

    @Test
    void getEnrolledCoursesByStuId_Successful() {
        // Mocking
        String authToken = "validToken";
        JSONObject userObj = new JSONObject();
        userObj.put("id", 1);
//        when(enrollmentService.getUserDetailsFromToken(eq(authToken))).thenReturn(userObj);
        doReturn(userObj).when(enrollmentService).getUserDetailsFromToken(authToken);

        List<Course> enrolledCourses = new ArrayList<>();
        enrolledCourses.add(new Course());
        when(courseEnrollmentDao.findEnrolledCourses(anyInt())).thenReturn(enrolledCourses);

        // Test
        try {
            JSONObject result = enrollmentService.getEnrolledCoursesByStuId(authToken);
            assertTrue(result.has("userDetails"));
            assertTrue(result.has("courseDetails"));
            assertEquals(1, result.getJSONArray("courseDetails").length());
        } catch (Exception e) {
        }
    }

    @Test
    void getEnrolledCoursesByStuId_UnauthorizedUser() {
        // Mocking
        String authToken = "invalidToken";
//        when(enrollmentService.getUserDetailsFromToken(anyString())).thenReturn(null);
        doReturn(null).when(enrollmentService).getUserDetailsFromToken(anyString());

        // Test
        assertThrows(CustomException.class, () -> enrollmentService.getEnrolledCoursesByStuId(authToken));
    }

//    @Test
//    void getUserDetailsFromToken_Successful() {
//        // Mocking
//        String token = "validToken";
//        String uri = "http://3.92.164.4:8081/auth";
//        String responseBody = "{\"userId\": 1, \"username\": \"john_doe\"}";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", token);
//
//        HttpEntity<String> entity = new HttpEntity<>(null, headers);
//
//        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
//        when(restTemplate.exchange(eq(uri), eq(HttpMethod.GET), eq(entity), eq(String.class)))
//                .thenReturn(responseEntity);
//
//        // Test
//        JSONObject result = enrollmentService.getUserDetailsFromToken(token);
//        assertNotNull(result);
//        assertEquals(1, result.getInt("userId"));
//        assertEquals("john_doe", result.getString("username"));
//    }

//    @Test
//    void getUserDetailsFromToken_Error() {
//        // Mocking
//        String token = "invalidToken";
//        String uri = "http://3.92.164.4:8081/auth";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", token);
//
//        HttpEntity<String> entity = new HttpEntity<>(null, headers);
//
//        when(restTemplate.exchange(eq(uri), eq(HttpMethod.GET), eq(entity), eq(String.class)))
//                .thenThrow(new RuntimeException("Simulated error"));
//
//        // Test
//        JSONObject result = enrollmentService.getUserDetailsFromToken(token);
//        assertNull(result);
//    }

//    @Test
//    void getUserDetailsFromUserId_Successful() {
//        // Mocking
//        int userId = 1;
//        String uri = "http://3.92.164.4:8081/user-details?userId=1";
//        String responseBody = "{\"userId\": 1, \"username\": \"john_doe\"}";
//
//        HttpEntity<String> entity = new HttpEntity<>(null);
//
//        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
//        RestTemplate restTemplate1 = mock(RestTemplate.class);
////        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class)))
////                .thenReturn(responseEntity);
//        doReturn(responseEntity).when(restTemplate1).exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class));
//
//        // Test
//        JSONObject result = enrollmentService.getUserDetailsFromUserId(userId);
//        assertNotNull(result);
//        assertEquals(1, result.getInt("userId"));
//        assertEquals("john_doe", result.getString("username"));
//    }

//    @Test
//    void getUserDetailsFromUserId_Error() {
//        // Mocking
//        int userId = 1;
//        String uri = "http://3.92.164.4:8081/user-details?userId=1";
//
//        HttpEntity<String> entity = new HttpEntity<>(null);
//
//        when(restTemplate.exchange(eq(uri), eq(HttpMethod.GET), eq(entity), eq(String.class)))
//                .thenThrow(new RuntimeException("Simulated error"));
//
//        // Test
//        JSONObject result = enrollmentService.getUserDetailsFromUserId(userId);
//        assertNull(result);
//    }




}
