package com.sam.termtracker.UI.form;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.sam.termtracker.BroadcastReceiver;
import com.sam.termtracker.DAO.AssessmentDAO;
import com.sam.termtracker.Database.Database;
import com.sam.termtracker.Entity.Assessment;
import com.sam.termtracker.Helper;
import com.sam.termtracker.R;
import com.sam.termtracker.UI.CourseInfoAssessmentViewActivity;


public class EditAssessmentActivity extends AppCompatActivity {
    TextInputEditText assessmentNameInput;
    TextInputLayout assessmentNameInputLayout;
    TextInputEditText startDateInput;
    TextInputLayout startDateLayout;
    TextInputEditText endDateInput;
    TextInputLayout endDateLayout;
    MaterialSwitch startDateSwitch;
    MaterialSwitch endDateSwitch;
    TextInputLayout assessmentTypeLayout;
    MaterialAutoCompleteTextView assessmentType;
    MaterialDatePicker<Long> startDatePicker;
    MaterialDatePicker<Long> endDatePicker;

    FloatingActionButton fab;
    String assessmentName;
    AlertDialog errorWithInputsDialog;
    ActionBar actionBar;

    Boolean editingAssessment;
    Boolean otherDateFieldEmpty;
    Boolean validStartDate;
    Boolean validEndDate;
    int startDateInTimestamp;
    int endDateInTimestamp;

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
        validStartDate = true;
        validEndDate = true;

        Database db = Database.getDatabase(getApplication());
        activeCourse = db.activeCourse;
        assessmentDAO = db.assessmentDAO();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            assessmentId = extras.getInt("id");
            assessment = assessmentDAO.getAssessmentById(assessmentId);
            loadInfoIntoFields(assessment);
            actionBar.setTitle("Edit Assessment");
            editingAssessment = true;
            try {
                Boolean showDetailedView = extras.getBoolean("detailedView");
                if (showDetailedView) {
                    actionBar.setTitle("Detailed Assessment View");
                }
            } catch (Exception e) {

            }
        } else {
            actionBar.setTitle("Add Assessment");
            editingAssessment = false;
        }
        initializeClickListeners();
    }

    private void initializeViews() {
        assessmentNameInput = findViewById(R.id.assessmentName);
        assessmentNameInputLayout = findViewById(R.id.assessmentNameLayout);
        startDateInput = findViewById(R.id.startDate);
        startDateLayout = findViewById(R.id.startDateLayout);
        endDateInput = findViewById(R.id.endDate);
        endDateLayout = findViewById(R.id.endDateLayout);
        startDateSwitch = findViewById(R.id.startDateSwitch);
        endDateSwitch = findViewById(R.id.endDateSwitch);
        assessmentTypeLayout = findViewById(R.id.assessmentTypeLayout);
        assessmentType = findViewById(R.id.assessmentType);

        fab = findViewById(R.id.floating_action_button);
        actionBar = getSupportActionBar();
        errorWithInputsDialog = new MaterialAlertDialogBuilder(this)
                .setTitle("Error")
                .setMessage("Please make sure all forms are filled out correctly")
                .setPositiveButton("Dismiss", ((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }))
                .create();
        startDatePicker = Helper.buildDatePicker(startDateInput);
        endDatePicker = Helper.buildDatePicker(endDateInput);
    }

    private void loadInfoIntoFields(Assessment assessment) {
        assessmentNameInput.setText(assessment.name);
        startDateInput.setText(Helper.epochToString(assessment.startDate));
        endDateInput.setText(Helper.epochToString(assessment.endDate));
        startDateSwitch.setChecked(assessment.notifyBeforeStart);
        endDateSwitch.setChecked(assessment.notifyBeforeEnd);
        assessmentType.setText(assessment.assessmentType, false);
    }

    private void initializeClickListeners() {
        startDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        endDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        startDateInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                otherDateFieldEmpty = endDateInput.getText().toString().isEmpty();
                startDateInTimestamp = Helper.dateTextToEpoch(startDateInput.getText().toString());

                if (!otherDateFieldEmpty) {
                    endDateInTimestamp = Helper.dateTextToEpoch(endDateInput.getText().toString());

                    if (startDateInTimestamp >= endDateInTimestamp) {
                        startDateLayout.setError("Start date must be before end date");
                        validStartDate = false;
                    } else {
                        validStartDate = true;
                        startDateLayout.setError(null);
                        endDateLayout.setError(null);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        endDateInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                otherDateFieldEmpty = startDateInput.getText().toString().isEmpty();
                endDateInTimestamp = Helper.dateTextToEpoch(endDateInput.getText().toString());

                if (!otherDateFieldEmpty) {
                    startDateInTimestamp = Helper.dateTextToEpoch(startDateInput.getText().toString());

                    if (startDateInTimestamp >= endDateInTimestamp) {
                        endDateLayout.setError("End date must be after start date");
                        validEndDate = false;
                    } else {
                        validEndDate = true;
                        startDateLayout.setError(null);
                        endDateLayout.setError(null);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void setDateNotifications(MaterialSwitch startDateSwitch, MaterialSwitch endDateSwitch) {
        Intent intent = new Intent(this, BroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int TWELVE_HOURS_IN_SECONDS = 43200;
        // reset the notifications
        alarmManager.cancel(pendingIntent);

        if (startDateSwitch.isChecked()) {
            long startDateInMillis = (startDateInTimestamp - TWELVE_HOURS_IN_SECONDS) * 1000;
            alarmManager.set(AlarmManager.RTC_WAKEUP, startDateInMillis, pendingIntent);
        }

        if (endDateSwitch.isChecked()) {
            long endDateInMillis = (endDateInTimestamp - TWELVE_HOURS_IN_SECONDS) * 1000;
            alarmManager.set(AlarmManager.RTC_WAKEUP, endDateInMillis, pendingIntent);
        }
    }

    public void saveAssessment(View view) {
        assessmentName = assessmentNameInput.getText().toString();
        setDateNotifications(startDateSwitch, endDateSwitch);
        // form validation
        if (assessmentName.isEmpty() || !validEndDate || !validStartDate) {
            errorWithInputsDialog.show();
            return;
        }

        startDateInTimestamp = Helper.dateTextToEpoch(startDateInput.getText().toString());
        endDateInTimestamp = Helper.dateTextToEpoch(endDateInput.getText().toString());

        // update or create new assessment
        if (editingAssessment) {
            assessment.name = assessmentName;
            assessment.startDate = startDateInTimestamp;
            assessment.endDate = endDateInTimestamp;
            assessment.notifyBeforeStart = startDateSwitch.isChecked();
            assessment.notifyBeforeEnd = endDateSwitch.isChecked();
            assessment.assessmentType = assessmentType.getText().toString();
            assessmentDAO.updateAssessment(assessment);
        } else {
            assessmentDAO.insertAssessment(new Assessment(assessmentName,
                    activeCourse,
                    startDateInTimestamp,
                    endDateInTimestamp,
                    startDateSwitch.isChecked(),
                    endDateSwitch.isChecked(),
                    assessmentType.getText().toString()));
        }
        Intent intent = new Intent(getApplicationContext(), CourseInfoAssessmentViewActivity.class);
        startActivity(intent);
    }
}