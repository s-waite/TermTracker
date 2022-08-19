package com.sam.termtracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.sam.termtracker.DAO.TermDAO;
import com.sam.termtracker.Database.Database;
import com.sam.termtracker.Entity.Course;
import com.sam.termtracker.Entity.Term;
import com.sam.termtracker.Helper;
import com.sam.termtracker.R;
import com.sam.termtracker.DAO.CourseDAO;

import java.util.List;

public class TermInfoCourseViewActivity extends AppCompatActivity {
    CourseDAO courseDao;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    MaterialTextView termName;
    MaterialTextView termStartDate;
    MaterialTextView termEndDate;
    TermDAO termDAO;
    int termId;
    Term term;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_info_course_view);


        getSupportActionBar().setTitle("Term Info and Courses");


        Database db = Database.getDatabase(getApplication());
        courseDao = db.courseDAO();
        termDAO = db.termDao();

//        courseDao.insertCourse(new Course("One", 10000, 10000, 1));

        List<Course> courseList = courseDao.getAll();


        termName = findViewById(R.id.termInfoName);
        termStartDate = findViewById(R.id.termInfoStartDate);
        termEndDate = findViewById(R.id.termInfoEndDate);

        termId = db.activeTerm;
        term = termDAO.getTermById(termId);

        termName.setText(term.termName);
        termStartDate.setText(Helper.epochToString(term.startDate));
        termEndDate.setText(Helper.epochToString(term.endDate));


        recyclerView = findViewById(R.id.courseRecycler);
        recyclerView.setAdapter(new CourseRecyclerAdapter(courseList, this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab = findViewById(R.id.courseFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditCourseActivity.class);
                startActivity(intent);
            }
        });


    }


}