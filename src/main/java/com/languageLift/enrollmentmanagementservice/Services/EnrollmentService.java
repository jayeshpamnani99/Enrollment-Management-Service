package com.languageLift.enrollmentmanagementservice.Services;

import com.languageLift.enrollmentmanagementservice.CustomException;
import com.languageLift.enrollmentmanagementservice.Dao.CourseDao;
import com.languageLift.enrollmentmanagementservice.Dao.CourseEnrollmentDao;
import com.languageLift.enrollmentmanagementservice.Models.Constants;
import com.languageLift.enrollmentmanagementservice.Models.Course;
import com.languageLift.enrollmentmanagementservice.Models.CourseEnrollment;
import jakarta.persistence.EntityManager;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class EnrollmentService {

    Logger logger = LoggerFactory.getLogger(EnrollmentService.class);

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private CourseEnrollmentDao courseEnrollmentDao;

    @Autowired
    private EntityManager entityManager;


    public List<Course> getAllCourses(String authToken) throws Exception {
        JSONObject userObj = getUserDetailsFromToken(authToken);
        if (null == userObj || userObj.isEmpty()){
            throw new CustomException(new Exception("User not Authorised"), HttpStatus.UNAUTHORIZED);
        }
        logger.info("user object : {}",userObj);
        List<Course> courseList = Streamable.of(courseDao.findAll()).toList();

        courseList.forEach(course -> {
            JSONObject instructorDetails = getUserDetailsFromUserId(course.getInstructorId());
            logger.info("instructor details : {}",instructorDetails);
            if (null != instructorDetails) {
                course.setInstructorDetails(instructorDetails.toMap());
            }
        });

        return courseList;

    }

    public boolean checkIfUserAlreadyRegisteredInCourse(int userId, int courseId){
        CourseEnrollment courseEnrollment = courseEnrollmentDao.
                findCourseEnrollmentByCourseIdAndAndStudentId(courseId, userId);
        if (null == courseEnrollment){
            return false;
        }
        return true;
    }

    public Course getCourseById(int courseId) {
        return entityManager.find(Course.class, courseId);
    }

    public int enroll(String authToken, int courseId) throws Exception{
        if (0 == courseId){
            throw new CustomException(new Exception("CourseId empty!"), HttpStatus.BAD_REQUEST);
        }
        JSONObject userObj = getUserDetailsFromToken(authToken);
        if (null == userObj || userObj.isEmpty()){
            throw new CustomException(new Exception("User not Authorised"), HttpStatus.UNAUTHORIZED);
        }

        if (!userObj.getString("roleName").equals("student")){
            throw new CustomException(new Exception("User is not a student"), HttpStatus.BAD_REQUEST);
        }

        logger.info("user object : {}",userObj);
        Course course = getCourseById(courseId);
        if (null == course){
            throw new CustomException(new Exception("Invalid CourseId!"), HttpStatus.NOT_FOUND);
        }

        if (checkIfUserAlreadyRegisteredInCourse(userObj.getInt("id"), courseId)){
            throw new CustomException(new Exception("User already registered in course"), HttpStatus.FORBIDDEN);
        }

        CourseEnrollment courseEnrollment = new CourseEnrollment();
        courseEnrollment.setCourseId(courseId);
        courseEnrollment.setStudentId(userObj.getInt("id"));

        CourseEnrollment saved = courseEnrollmentDao.save(courseEnrollment);
        return saved.getId();
    }

    public JSONObject getEnrolledCoursesByStuId(String authToken) throws Exception {
        JSONObject userObj = getUserDetailsFromToken(authToken);
        if (null == userObj || userObj.isEmpty()){
            throw new CustomException(new Exception("User not Authorised"), HttpStatus.UNAUTHORIZED);
        }
        logger.info("user object : {}",userObj);
        JSONObject res = new JSONObject();
        res.put("userDetails", userObj);

        List<Course> courseList = courseEnrollmentDao.findEnrolledCourses(userObj.getInt("id"));


        courseList.forEach(course -> {
            JSONObject instructorDetails = getUserDetailsFromUserId(course.getInstructorId());
            logger.info("instructor details : {}",instructorDetails);
            if (null != instructorDetails) {
                course.setInstructorDetails(instructorDetails.toMap());
            }
        });

        res.put("courseDetails", courseList);

        return res;

    }

    public JSONObject getEnrolledCoursesByInstructorId(String authToken) throws Exception {
        JSONObject userObj = getUserDetailsFromToken(authToken);
        if (null == userObj || userObj.isEmpty()){
            throw new CustomException(new Exception("User not Authorised"), HttpStatus.UNAUTHORIZED);
        }
        logger.info("user object : {}",userObj);
        JSONObject res = new JSONObject();
        res.put("userDetails", userObj);

        List<Course> courseList = courseEnrollmentDao.findCoursesByInstructorId(userObj.getInt("id"));

        res.put("courseDetails", courseList);

        return res;

    }

    public static List<Course> removeEnrolledCourses(List<Course> courseList, List<Course> enrolledCourseList) {
        HashSet<Integer> enrolledCourseIds = new HashSet<>();
        for (Course course : enrolledCourseList) {
            enrolledCourseIds.add(course.getId());
        }

        List<Course> filteredCourseList = new ArrayList<>();
        for (Course course : courseList) {
            if (!enrolledCourseIds.contains(course.getId())) {
                filteredCourseList.add(course);
            }
        }

        return filteredCourseList;
    }

    public JSONObject getNotEnrolledCoursesByStuId(String authToken) throws Exception {
        JSONObject userObj = getUserDetailsFromToken(authToken);
        if (null == userObj || userObj.isEmpty()){
            throw new CustomException(new Exception("User not Authorised"), HttpStatus.UNAUTHORIZED);
        }
        logger.info("user object : {}",userObj);
        JSONObject res = new JSONObject();
        res.put("userDetails", userObj);

        List<Course> courseList = Streamable.of(courseDao.findAll()).toList();
        List<Course> enrolledCourseList = courseEnrollmentDao.findEnrolledCourses(userObj.getInt("id"));

        List<Course> filteredCourseList = removeEnrolledCourses(courseList, enrolledCourseList);


        filteredCourseList.forEach(course -> {
            JSONObject instructorDetails = getUserDetailsFromUserId(course.getInstructorId());
            logger.info("instructor details : {}",instructorDetails);
            if (null != instructorDetails) {
                course.setInstructorDetails(instructorDetails.toMap());
            }
        });

        res.put("courseDetails", filteredCourseList);

        return res;

    }

    public JSONObject getUserDetailsFromToken(String token) {
        final String uri = "http://" + Constants.AWS_IP + ":8081/auth";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);


        try {
            ResponseEntity<String> res = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            String resBody = res.getBody();
            JSONObject jsonObject = new JSONObject(resBody);
            return jsonObject;
        } catch (Exception e) {
            logger.error("Error in API for auth!");
            return null;
        }
    }

    public JSONObject getUserDetailsFromUserId(int userId) {
        final String uri = "http://" + Constants.AWS_IP + ":8081/user-details?userId="+ userId;

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(null);


        try {
            ResponseEntity<String> res = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            String resBody = res.getBody();
            JSONObject jsonObject = new JSONObject(resBody);
            logger.info("response body : {}",jsonObject);
            return jsonObject;
        } catch (Exception e) {
            logger.error("Error in API for getting user details from userId!");
            return null;
        }
    }

}
