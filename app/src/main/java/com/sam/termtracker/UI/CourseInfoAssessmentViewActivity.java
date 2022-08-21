package com.sam.termtracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textview.MaterialTextView;
import com.sam.termtracker.DAO.CourseDAO;
import com.sam.termtracker.Database.Database;
import com.sam.termtracker.Entity.Course;
import com.sam.termtracker.Helper;
import com.sam.termtracker.R;

import android.os.Bundle;

public class CourseInfoAssessmentViewActivity extends AppCompatActivity {
    MaterialTextView courseName;
    MaterialTextView courseStartDate;
    MaterialTextView courseEndDate;
    MaterialTextView courseStatus;
    MaterialTextView instructorName;
    MaterialTextView instructorEmail;
    MaterialTextView instructorPhone;

    Database db;
    Course course;
    CourseDAO courseDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info_assessment_view);

        Database db = Database.getDatabase(getApplication());
        courseDAO = db.courseDAO();
        course = courseDAO.getCourseById(db.activeCourse);

        initializeViews();
        loadCourseInfoIntoViews(course);
    }

    private void initializeViews() {
        courseName = findViewById(R.id.courseName);
        courseStartDate = findViewById(R.id.courseStartDate);
        courseEndDate = findViewById(R.id.courseEndDate);
        courseStatus = findViewById(R.id.courseStatus);
        instructorName = findViewById(R.id.instructorName);
        instructorEmail = findViewById(R.id.instructorEmail);
        instructorPhone = findViewById(R.id.instructorPhone);
    }

    private void loadCourseInfoIntoViews(Course course) {
        courseName.setText(course.name);
        courseStartDate.setText(Helper.epochToString(course.startDate));
        courseEndDate.setText(Helper.epochToString(course.endDate));
        courseStatus.setText(course.courseStatus);
        instructorName.setText(course.instructorName);
        instructorEmail.setText(course.instructorEmail);
        instructorPhone.setText(course.instructorPhone);
    }
}