package com.languageLift.enrollmentmanagementservice.Dao;

import com.languageLift.enrollmentmanagementservice.Models.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseDao extends CrudRepository <Course,Integer>
{

}
