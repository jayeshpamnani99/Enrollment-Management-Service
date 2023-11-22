package com.languageLift.enrollmentmanagementservice.Resource;

import com.languageLift.enrollmentmanagementservice.CustomException;
import com.languageLift.enrollmentmanagementservice.Services.EnrollmentService;
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

@RestController
public class EnrollmentResource
{
    Logger logger = LoggerFactory.getLogger(EnrollmentResource.class);

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/auth")
    public ResponseEntity<User> checkIfUserIsAuthorised(@RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) throws Exception {
        // code that uses the language variable
        User user = userService.getUserFromJWTToken(authToken);

        return new ResponseEntity<User>(user, HttpStatus.OK);
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
