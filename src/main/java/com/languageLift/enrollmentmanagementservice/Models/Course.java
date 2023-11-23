package com.languageLift.enrollmentmanagementservice.Models;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="course")
public class Course
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private Integer instructorId;
    private String description;
    private Integer avgDuration;
    private String difficulty;

    @Transient
    private HashMap instructorDetails;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAvgDuration() {
        return avgDuration;
    }

    public void setAvgDuration(Integer avgDuration) {
        this.avgDuration = avgDuration;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Map getInstructorDetails() {
        return instructorDetails;
    }

    public void setInstructorDetails(Map instructorDetails) {
        this.instructorDetails = new HashMap(instructorDetails);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", instructorId=" + instructorId +
                ", description='" + description + '\'' +
                ", avgDuration=" + avgDuration +
                ", difficulty='" + difficulty + '\'' +
                ", instructorDetails=" + instructorDetails +
                '}';
    }

}
