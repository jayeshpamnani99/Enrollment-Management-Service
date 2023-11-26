package com.languageLift.enrollmentmanagementservice.Dao;

import com.languageLift.enrollmentmanagementservice.Models.Course;
import com.languageLift.enrollmentmanagementservice.Models.CourseEnrollment;
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseEnrollmentDao extends CrudRepository <CourseEnrollment,Integer>
{
    @Query(value = "SELECT c FROM Course c JOIN CourseEnrollment ce ON c.id = ce.courseId WHERE ce.studentId = :studentId", nativeQuery = false)
    public List<Course> findEnrolledCourses(@Param("studentId") Integer studentId);

    @Query(value = "SELECT c FROM Course c WHERE c.instructorId = :instructorId", nativeQuery = false)
    public List<Course> findCoursesByInstructorId(@Param("instructorId") Integer instructorId);



    @Query(value = "SELECT ce FROM CourseEnrollment ce WHERE ce.courseId=:courseId AND ce.studentId=:studentId")
    public CourseEnrollment findCourseEnrollmentByCourseIdAndAndStudentId(@Param("courseId") int courseId, @Param("studentId") int studentId);

    @Modifying
    @Query(value = "INSERT INTO course_enrollment (courseId, studentId, status, enrollmentDate) " +
            "VALUES (:courseId, :studentId, 'Enrolled', CURRENT_DATE())", nativeQuery = true)
    public void enrollStudentInCourse(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);

}
