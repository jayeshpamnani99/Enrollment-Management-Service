package com.languageLift.enrollmentmanagementservice.Dao;

import com.languageLift.enrollmentmanagementservice.Models.Course;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseEnrollmentDao extends CrudRepository <Course,Integer>
{
    @Query("SELECT c.* FROM courses c INNER JOIN course_enrollment ce ON c.id = ce.courseId WHERE ce.studentId = :studentId")
    public List<Course> findEnrolledCourses(@Param("studentId") Integer studentId);

    @Modifying
    @Query("INSERT INTO course_enrollment (courseId, studentId, instructorId, status, enrollmentDate) " +
            "VALUES (:courseId, :studentId, (SELECT instructorId FROM courses WHERE id = :courseId), 'Enrolled', CURRENT_DATE())")
    public void enrollStudentInCourse(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);

}
