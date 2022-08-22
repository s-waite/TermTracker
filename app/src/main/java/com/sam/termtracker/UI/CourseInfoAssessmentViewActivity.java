package com.sam.termtracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.sam.termtracker.DAO.AssessmentDAO;
import com.sam.termtracker.DAO.CourseDAO;
import com.sam.termtracker.Database.Database;
import com.sam.termtracker.Entity.Assessment;
import com.sam.termtracker.Entity.Course;
import com.sam.termtracker.Helper;
import com.sam.termtracker.R;
import com.sam.termtracker.UI.recyclerAdapter.AssessmentRecyclerAdapter;
import com.sam.termtracker.UI.recyclerAdapter.CourseRecyclerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class CourseInfoAssessmentViewActivity extends AppCompatActivity {
    MaterialTextView courseName;
    MaterialTextView courseStartDate;
    MaterialTextView courseEndDate;
    MaterialTextView courseStatus;
    MaterialTextView instructorName;
    MaterialTextView instructorEmail;
    MaterialTextView instructorPhone;

    RecyclerView recyclerView;
    List<Assessment> assessmentList;

    FloatingActionButton fab;

    Database db;
    Course course;
    int activeCourse;
    CourseDAO courseDAO;
    AssessmentDAO assessmentDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info_assessment_view);

        Database db = Database.getDatabase(getApplication());
        activeCourse = db.activeCourse;
        courseDAO = db.courseDAO();
        assessmentDAO = db.assessmentDAO();
        course = courseDAO.getCourseById(activeCourse);

        initializeViews();
        loadCourseInfoIntoViews(course);
        initializeClickListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initializeViews() {
        courseName = findViewById(R.id.courseName);
        courseStartDate = findViewById(R.id.courseStartDate);
        courseEndDate = findViewById(R.id.courseEndDate);
        courseStatus = findViewById(R.id.courseStatus);
        instructorName = findViewById(R.id.instructorName);
        instructorEmail = findViewById(R.id.instructorEmail);
        instructorPhone = findViewById(R.id.instructorPhone);
        fab = findViewById(R.id.assessmentFab);
        assessmentList = assessmentDAO.getAllByCourseId(activeCourse);
        recyclerView = findViewById(R.id.assessmentRecycler);
        recyclerView.setAdapter(new AssessmentRecyclerAdapter(assessmentList, this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

    private void initializeClickListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditAssessmentActivity.class);
                startActivity(intent);
            }
        });
    }
}