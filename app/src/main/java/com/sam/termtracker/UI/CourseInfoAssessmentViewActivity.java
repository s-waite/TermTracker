package com.sam.termtracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textview.MaterialTextView;
import com.sam.termtracker.R;

import android.os.Bundle;

public class CourseInfoAssessmentViewActivity extends AppCompatActivity {
    MaterialTextView courseName;
    MaterialTextView courseStartDate;
    MaterialTextView courseEndDate;
    MaterialTextView courseStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info_assessment_view);
    }

    private void initializeViews() {

    }
}