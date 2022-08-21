package com.sam.termtracker.UI;


import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sam.termtracker.DAO.CourseDAO;
import com.sam.termtracker.DAO.TermDAO;
import com.sam.termtracker.Database.Database;
import com.sam.termtracker.Entity.Course;
import com.sam.termtracker.Entity.Term;
import com.sam.termtracker.Helper;
import com.sam.termtracker.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class EditCourseActivity extends AppCompatActivity {
    TextInputEditText courseNameInput;
    TextInputEditText startDateInput;
    TextInputEditText endDateInput;
    TextInputEditText instructorNameInput;
    TextInputEditText instructorEmailInput;
    TextInputEditText instructorPhoneInput;

    TextInputLayout startDateInputLayout;
    TextInputLayout endDateInputLayout;
    TextInputLayout instructorNameInputLayout;
    TextInputLayout instructorEmailInputLayout;
    TextInputLayout instructorPhoneInputLayout;
    Course course;
    Boolean newCourse;
    Boolean formHasError;
    Boolean nameHasError;
    Boolean otherDateFieldEmpty;
    Database db;
    CourseDAO courseDAO;
    MaterialDatePicker<Long> startDatePicker;
    MaterialDatePicker<Long> endDatePicker;
    int startDateInTimestamp;
    int endDateInTimestamp;
    ActionBar actionBar;
    AlertDialog errorWithInputsDialog;
    MaterialDatePicker<Long> datePicker;


    /**
     * Activity for creating and editing terms
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        // Get a reference to the DAO
        db = Database.getDatabase(getApplication());
        courseDAO = db.courseDAO();

        actionBar = getSupportActionBar();
        initializeViews();

        // Create two date pickers that register confirmation listeners that change the supplied
        // text input to the result of the date picker
        startDatePicker = Helper.buildDatePicker(startDateInput);
        endDatePicker = Helper.buildDatePicker(endDateInput);


        // Check if active course is null
        // If it is then we are creating a new course
        if (db.activeCourse != null) {
            course = courseDAO.getCourseById(db.activeCourse);
            loadTermInfoIntoFields(course);
            // this bool keeps track of whether we are editing or creating a new term
            newCourse = false;
            actionBar.setTitle("Edit Course");
        } else {
            newCourse = true;
            actionBar.setTitle("Add Course");
        }
        initializeClickListeners();
    }

    /**
     * Called in onCreate, get references to the views of the layout and define the click listeners
     */
    private void initializeViews() {
        courseNameInput = findViewById(R.id.courseName);
        endDateInput = findViewById(R.id.endDateCourse);
        startDateInput = findViewById(R.id.startDateCourse);
        instructorNameInput = findViewById(R.id.instructorName);
        instructorEmailInput = findViewById(R.id.instructorEmail);
        instructorPhoneInput = findViewById(R.id.instructorPhone);


        startDateInputLayout = findViewById(R.id.startDateCourseLayout);
        endDateInputLayout = findViewById(R.id.endDateCourseLayout);
        instructorNameInputLayout = findViewById(R.id.instructorNameLayout);
        instructorEmailInputLayout = findViewById(R.id.instructorEmailLayout);
        instructorPhoneInputLayout = findViewById(R.id.instructorPhoneLayout);


        errorWithInputsDialog = new MaterialAlertDialogBuilder(this)
                .setTitle("Error")
                .setMessage("Please make sure all forms are filled out correctly")
                .setPositiveButton("Dismiss", ((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }))
                .create();
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
                        startDateInputLayout.setError("Start date must be before end date");
                        formHasError = true;
                    } else {
                        formHasError = false;
                        startDateInputLayout.setError(null);
                        endDateInputLayout.setError(null);
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
                        endDateInputLayout.setError("End date must be after start date");
                        formHasError = true;
                    } else {
                        formHasError = false;
                        startDateInputLayout.setError(null);
                        endDateInputLayout.setError(null);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private Boolean checkForErrors() {
        return true;
    }

    /**
     * When editing a course, loads the info from the term into the edit form.
     *
     * @param course the course to load data from
     */
    private void loadTermInfoIntoFields(Course course) {
        String startDate = Helper.epochToString(course.startDate);
        String endDate = Helper.epochToString(course.endDate);
        String courseName = course.name;
        String instructorName = course.instructorName;
        String instructorEmail = course.instructorEmail;
        String instructorPhone = course.instructorPhone;

        courseNameInput.setText(courseName);
        startDateInput.setText(startDate);
        endDateInput.setText(endDate);
        instructorNameInput.setText(instructorName);
        instructorEmailInput.setText(instructorEmail);
        instructorPhoneInput.setText(instructorPhone);
    }

    /**
     * onClick method for the save FAB.
     * <p>
     * Creates a new term object of updates an existing one
     *
     * @param view
     */
    public void saveCourse(View view) {
        // Check if term name is empty
        if (courseNameInput.getText().toString().isEmpty()) {
            nameHasError = true;
        } else {
            nameHasError = false;
        }

        if (formHasError || nameHasError) {
            errorWithInputsDialog.show();
            return;
        }

        formHasError = checkForErrors();

        startDateInTimestamp = Helper.dateTextToEpoch(startDateInput.getText().toString());
        endDateInTimestamp = Helper.dateTextToEpoch(endDateInput.getText().toString());

        if (!newCourse) {
            course.name = courseNameInput.getText().toString();
            course.startDate = startDateInTimestamp;
            course.endDate = endDateInTimestamp;
            course.instructorName = instructorNameInput.getText().toString();
            course.instructorEmail = instructorEmailInput.getText().toString();
            course.instructorPhone = instructorPhoneInput.getText().toString();
            courseDAO.updateCourse(course);
        } else {
            courseDAO.insertCourse(new Course(
                    courseNameInput.getText().toString(),
                    startDateInTimestamp,
                    endDateInTimestamp,
                    db.activeTerm,
                    instructorNameInput.getText().toString(),
                    instructorEmailInput.getText().toString(),
                    instructorPhoneInput.getText().toString()));
        }

        // now that we are finished with the course we can set it as null so that it is not loaded
        // into the add/edit course form when we want to add a new course
        db.activeCourse = null;
        Intent intent = new Intent(this, TermInfoCourseViewActivity.class);
        startActivity(intent);
    }

}
