package com.languageLift.enrollmentmanagementservice.Resource;

import com.languageLift.enrollmentmanagementservice.CustomException;
import com.languageLift.enrollmentmanagementservice.Models.Course;
import com.languageLift.enrollmentmanagementservice.Services.EnrollmentService;
import jakarta.websocket.server.PathParam;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class EnrollmentResource
{
    Logger logger = LoggerFactory.getLogger(EnrollmentResource.class);

    @Autowired
    private EnrollmentService enrollmentService;


    @GetMapping("/getAllCourses")
    public ResponseEntity<List> getAllCourses(@RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) throws Exception {
        // code that uses the language variable
        List<Course> allCourses = enrollmentService.getAllCourses(authToken);

        return new ResponseEntity<List>(allCourses, HttpStatus.OK);
    }

    @GetMapping("/getEnrolledCoursesByStuId")
    public ResponseEntity<Map> getEnrolledCoursesByStuId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) throws Exception {
        // code that uses the language variable
        JSONObject enrolledCourses = enrollmentService.getEnrolledCoursesByStuId(authToken);

        return new ResponseEntity<Map>(enrolledCourses.toMap(), HttpStatus.OK);
    }

    @GetMapping("/getNotEnrolledCoursesByStuId")
    public ResponseEntity<Map> getNotEnrolledCoursesByStuId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) throws Exception {
        // code that uses the language variable
        JSONObject notEnrolledCourses = enrollmentService.getNotEnrolledCoursesByStuId(authToken);

        return new ResponseEntity<Map>(notEnrolledCourses.toMap(), HttpStatus.OK);
    }


    @GetMapping("/enroll")
    public ResponseEntity<Map> enroll(@RequestHeader(HttpHeaders.AUTHORIZATION) String authToken, @PathParam("courseId") int courseId) throws Exception {
        // code that uses the language variable
        int enrollmentId = enrollmentService.enroll(authToken, courseId);
        JSONObject res = new JSONObject();
        res.put("enrollmentId", enrollmentId);
        res.put("message", "Successfully enrolled in course!");

        return new ResponseEntity<Map>(res.toMap(), HttpStatus.OK);
    }



    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleException(CustomException exception) {
        logger.info("inside exception handler: {}",exception);
        JSONObject obj = new JSONObject();
        obj.put("status",exception.getStatus());
        obj.put("message",exception.getE().getMessage());
        return ResponseEntity
                .status(exception.getStatus())
                .body(obj.toMap());
    }
}
