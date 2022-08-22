package com.sam.termtracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.sam.termtracker.DAO.AssessmentDAO;
import com.sam.termtracker.Database.Database;
import com.sam.termtracker.Entity.Assessment;
import com.sam.termtracker.R;


public class EditAssessmentActivity extends AppCompatActivity {
    TextInputEditText assessmentNameInput;
    TextInputLayout assessmentNameInputLayout;
    FloatingActionButton fab;
    String assessmentName;

    Database db;
    int activeCourse;
    AssessmentDAO assessmentDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);

        initializeViews();

        Database db = Database.getDatabase(getApplication());
        activeCourse = db.activeCourse;
        assessmentDAO = db.assessmentDAO();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            assessmentName = extras.getString("name");
            assessmentNameInput.setText(assessmentName);
        }

    }

    private void initializeViews() {
        assessmentNameInput = findViewById(R.id.assessmentName);
        assessmentNameInputLayout = findViewById(R.id.assessmentNameLayout);
        fab = findViewById(R.id.floating_action_button);
    }

    public void saveAssessment(View view) {
        assessmentDAO.insertAssessment(new Assessment(assessmentNameInput.getText().toString(), activeCourse));
        Intent intent = new Intent(getApplicationContext(), CourseInfoAssessmentViewActivity.class);
        startActivity(intent);
    }
}