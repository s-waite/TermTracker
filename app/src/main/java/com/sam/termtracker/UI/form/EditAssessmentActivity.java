package com.sam.termtracker.UI.form;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.sam.termtracker.DAO.AssessmentDAO;
import com.sam.termtracker.Database.Database;
import com.sam.termtracker.Entity.Assessment;
import com.sam.termtracker.R;
import com.sam.termtracker.UI.CourseInfoAssessmentViewActivity;


public class EditAssessmentActivity extends AppCompatActivity {
    TextInputEditText assessmentNameInput;
    TextInputLayout assessmentNameInputLayout;
    FloatingActionButton fab;
    String assessmentName;
    AlertDialog errorWithInputsDialog;

    Boolean editingAssessment;

    Database db;
    int activeCourse;
    int assessmentId;
    Assessment assessment;
    AssessmentDAO assessmentDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);

        initializeViews();

        Database db = Database.getDatabase(getApplication());
        activeCourse = db.activeCourse;
        assessmentDAO = db.assessmentDAO();
        editingAssessment = false;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            assessmentId = extras.getInt("id");
            assessment = assessmentDAO.getAssessmentById(assessmentId);
            assessmentNameInput.setText(assessment.name);
            editingAssessment = true;
        }
    }

    private void initializeViews() {
        assessmentNameInput = findViewById(R.id.assessmentName);
        assessmentNameInputLayout = findViewById(R.id.assessmentNameLayout);
        fab = findViewById(R.id.floating_action_button);
        errorWithInputsDialog = new MaterialAlertDialogBuilder(this)
                .setTitle("Error")
                .setMessage("Please enter a name for the assessment")
                .setPositiveButton("Dismiss", ((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }))
                .create();
    }

    public void saveAssessment(View view) {
        assessmentName = assessmentNameInput.getText().toString();
        // form validation
        if (assessmentName.isEmpty()) {
            errorWithInputsDialog.show();
            return;
        }

        // update or create new assessment
        if (editingAssessment) {
            assessment.name = assessmentName;
            assessmentDAO.updateAssessment(assessment);
        } else {
            assessmentDAO.insertAssessment(new Assessment(assessmentName, activeCourse));
        }
        Intent intent = new Intent(getApplicationContext(), CourseInfoAssessmentViewActivity.class);
        startActivity(intent);
    }
}