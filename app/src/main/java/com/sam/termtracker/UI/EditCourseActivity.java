package com.sam.termtracker.UI;


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
    TextInputLayout endDateInputLayout;
    TextInputLayout startDateInputLayout;
    Course course;
    Boolean newCourse;
    Boolean formHasError;
    Database db;
    CourseDAO courseDAO;
    MaterialDatePicker<Long> startDatePicker;
    MaterialDatePicker<Long> endDatePicker;
    int startDateInTimestamp;
    int endDateInTimestamp;
    int termId;
    ActionBar actionBar;
    AlertDialog errorWithInputsDialog;


    /**
     * Activity for creating and editing courses
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        // Get a reference to our DAO
        db = Database.getDatabase(getApplication());
        courseDAO = db.courseDAO();

        actionBar = getSupportActionBar();

        initializeViews();

        // Check if any extras are passed. If they are, this activity is editing a course
        // If not then we are creating a new course
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            course = loadCourseInfoIntoFields(extras);
          termId = extras.getInt("termId");
            // this bool keeps track of whether we are editing or creating a new course
            newCourse = false;
            actionBar.setTitle("Edit Course");
        } else {
            newCourse = true;
            actionBar.setTitle("Add Course");
        }
    }

    /**
     * Called in onCreate, get references to the views of the layout and define the click listeners
     */
    private void initializeViews() {
        courseNameInput = findViewById(R.id.courseName);
        startDateInput = findViewById(R.id.startDateCourse);
        endDateInput = findViewById(R.id.endDateCourse);
        endDateInputLayout = findViewById(R.id.endDateCourseLayout);
        startDateInputLayout = findViewById(R.id.startDateCourseLayout);

        // Use the Helper.changeDate() function to start a date picker
        // The changeDate function returns a MaterialDatePicker that can be used on subsequent presses of the field
        // This prevents creating a new MaterialDatePicker object every time
        // Passes the classes date picker to the helper function
        startDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDatePicker = Helper.changeDate(startDatePicker, startDateInput, getSupportFragmentManager());
            }
        });

        // Similar to the click listener of startDateInput
        endDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endDatePicker = Helper.changeDate(endDatePicker, endDateInput, getSupportFragmentManager());
            }
        });

        startDateInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    startDateInTimestamp = Helper.dateTextToEpoch(startDateInput.getText().toString());
                    endDateInTimestamp = Helper.dateTextToEpoch(endDateInput.getText().toString());
                    if (startDateInTimestamp >= endDateInTimestamp) {
                        startDateInputLayout.setError("Start date must be before end date");
                        formHasError = true;
                    } else {
                        formHasError = false;
                        startDateInputLayout.setError(null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
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

            // Check if the end date entered is after the start date
            // If it is not display an error
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                startDateInTimestamp = Helper.dateTextToEpoch(startDateInput.getText().toString());
                endDateInTimestamp = Helper.dateTextToEpoch(endDateInput.getText().toString());
                if (startDateInTimestamp >= endDateInTimestamp) {
                    endDateInputLayout.setError("End date must be after start date");
                    formHasError = true;
                } else {
                    formHasError = false;
                    endDateInputLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    /**
     * When editing a course, loads the info from the course into the edit form.
     *
     * @param extras the data passed to the activity
     * @return the course object that was passed as an extra from another activity to this
     */
    private Course loadCourseInfoIntoFields(Bundle extras) {
        DateTimeFormatter formatter = Helper.formatter;
        int courseId = extras.getInt("courseId");
         course = courseDAO.getCourseById(courseId);

        LocalDateTime startDateTime = LocalDateTime.ofEpochSecond(course.startDate, 0, ZoneOffset.UTC);
        String startDate = formatter.format(startDateTime);
        LocalDateTime endDateTime = LocalDateTime.ofEpochSecond(course.endDate, 0, ZoneOffset.UTC);
        String endDate = formatter.format(endDateTime);

        String courseName = course.name;

        courseNameInput.setText(courseName);
        startDateInput.setText(startDate);
        endDateInput.setText(endDate);
        return course;
    }

    /**
     * onClick method for the save FAB.
     * <p>
     * Creates a new course object of updates an existing one
     *
     * @param view
     */
    public void saveCourse(View view) {
        if (formHasError) {
            // create an error dialog if one has not already been created
            if (errorWithInputsDialog == null) {
                errorWithInputsDialog = new MaterialAlertDialogBuilder(this)
                        .setTitle("Error")
                        .setMessage("Please make sure all forms are filled out correctly")
                        .setPositiveButton("Dismiss", ((dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        }))
                        .create();
            }
            errorWithInputsDialog.show();
            return;
        }

        startDateInTimestamp = Helper.dateTextToEpoch(startDateInput.getText().toString());
        endDateInTimestamp = Helper.dateTextToEpoch(endDateInput.getText().toString());

        if (!newCourse) {
            course.name = courseNameInput.getText().toString();
            course.startDate = startDateInTimestamp;
            course.endDate = endDateInTimestamp;
            courseDAO.updateCourse(course);
        } else {
            courseDAO.insertCourse(new Course(courseNameInput.getText().toString(), startDateInTimestamp, endDateInTimestamp, termId));
        }
        Intent intent = new Intent(this, TermInfoCourseViewActivity.class);
        startActivity(intent);
    }
}
