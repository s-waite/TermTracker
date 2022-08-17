package com.sam.termtracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sam.termtracker.Database.Database;
import com.sam.termtracker.Entity.Course;
import com.sam.termtracker.R;
import com.sam.termtracker.DAO.CourseDAO;

import java.util.List;

public class TermInfoCourseViewActivity extends AppCompatActivity {
    CourseDAO courseDao;
    RecyclerView recyclerView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_info_course_view);

        getSupportActionBar().setTitle("Term Info and Coursesj");


        Database db = Database.getDatabase(getApplication());
        courseDao = db.courseDAO();

        courseDao.insertCourse(new Course("One", 10000, 10000, 1));

        List<Course> courseList = courseDao.getAll();






        recyclerView = findViewById(R.id.courseRecycler);
        recyclerView.setAdapter(new CourseRecyclerAdapter(courseList, this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab = findViewById(R.id.courseFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditTermActivity.class);
                startActivity(intent);
            }
        });
    }
}