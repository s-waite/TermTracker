package com.sam.termtracker.UI;


import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
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
    MaterialAutoCompleteTextView courseStatusInput;

    TextInputLayout courseNameInputLayout;
    TextInputLayout startDateInputLayout;
    TextInputLayout endDateInputLayout;
    TextInputLayout instructorNameInputLayout;
    TextInputLayout instructorEmailInputLayout;
    TextInputLayout instructorPhoneInputLayout;
    TextInputLayout courseStatusInputLayout;

    Pattern emailPattern;
    Pattern phonePattern;
    Boolean validName;
    Boolean validStartDate;
    Boolean validEndDate;
    Boolean validInstructorName;
    Boolean validInstructorEmail;
    Boolean validInstructorPhone;
    Boolean validCourseStatus;

    Course course;
    Boolean newCourse;
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

        emailPattern = Pattern.compile("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$", Pattern.CASE_INSENSITIVE);
        phonePattern = Pattern.compile("[0-9]{3}-[0-9]{3}-[0-9]{4}");
        validStartDate = false;
        validEndDate = false;

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
        courseStatusInput = findViewById(R.id.courseStatus);

        courseNameInputLayout = findViewById(R.id.courseNameLayout);
        startDateInputLayout = findViewById(R.id.startDateCourseLayout);
        endDateInputLayout = findViewById(R.id.endDateCourseLayout);
        instructorNameInputLayout = findViewById(R.id.instructorNameLayout);
        instructorEmailInputLayout = findViewById(R.id.instructorEmailLayout);
        instructorPhoneInputLayout = findViewById(R.id.instructorPhoneLayout);
        courseStatusInputLayout = findViewById(R.id.courseStatusLayout);


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
                        validStartDate = true;
                    } else {
                        validStartDate = false;
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
                        validEndDate = true;
                    } else {
                        validEndDate = false;
                        startDateInputLayout.setError(null);
                        endDateInputLayout.setError(null);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        instructorEmailInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    validInstructorEmail = emailPattern.matcher(instructorEmailInput.getText().toString()).find();
                    if (!validInstructorEmail) {
                        instructorEmailInputLayout.setError("Please enter a valid email");
                    } else {
                        instructorEmailInputLayout.setError(null);
                    }
                }
            }
        });

        instructorPhoneInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    validInstructorPhone = phonePattern.matcher(instructorPhoneInput.getText().toString()).find();
                    if (!validInstructorPhone) {
                        instructorPhoneInputLayout.setError("Please enter a valid number in the form: 123-456-7890");

                    } else {
                        instructorPhoneInputLayout.setError(null);
                    }
                }
            }
        });


    }

    private void clearFormErrors() {
        TextInputLayout[] textInputLayouts = {
                courseNameInputLayout,
                startDateInputLayout,
                endDateInputLayout,
                instructorNameInputLayout,
                instructorEmailInputLayout,
                instructorPhoneInputLayout,
                courseStatusInputLayout
        };

        for (TextInputLayout layout : textInputLayouts) {
            layout.setError(null);
        }
    }

    private Boolean formHasErrors() {
        validName = !(courseNameInput.getText().toString().isEmpty());
        if (!validName) {
            courseNameInputLayout.setError("Please enter the course name");
        }

        validStartDate = Helper.dateTextToEpoch(startDateInput.getText().toString()) < Helper.dateTextToEpoch(endDateInput.getText().toString());

        validEndDate = validStartDate;

        validInstructorName = !(instructorNameInput.getText().toString().isEmpty());
        if (!validInstructorName) {
            instructorNameInputLayout.setError("Please enter the instructors name");
        }

        validInstructorEmail = emailPattern.matcher(instructorEmailInput.getText().toString()).find();
        if (!validInstructorEmail) {
            instructorEmailInputLayout.setError("Please enter a valid email");
        }

        validInstructorPhone = phonePattern.matcher(instructorPhoneInput.getText().toString()).find();
        if (!validInstructorPhone) {
            instructorPhoneInputLayout.setError("Please enter a valid number in the form: 123-456-7890");
        }

        validCourseStatus = !(courseStatusInput.getText().toString().isEmpty());
        if (!validCourseStatus) {
            courseStatusInputLayout.setError("Please select the course status");
        }

        return (validName &&
                validStartDate &&
                validEndDate &&
                validInstructorName &&
                validInstructorEmail &&
                validInstructorPhone &&
                validCourseStatus
        );
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
        String courseStatus = course.courseStatus;

        courseNameInput.setText(courseName);
        startDateInput.setText(startDate);
        endDateInput.setText(endDate);
        instructorNameInput.setText(instructorName);
        instructorEmailInput.setText(instructorEmail);
        instructorPhoneInput.setText(instructorPhone);
        courseStatusInput.setText(courseStatus, false);
    }

    /**
     * * onClick method for the save FAB.
     * <p>
     * Creates a new term object of updates an existing one
     *
     * @param view
     */
    public void saveCourse(View view) {
        // Clear errors from previous save attempts
        clearFormErrors();

        if (!formHasErrors()) {
            errorWithInputsDialog.show();
            return;
        }
        startDateInTimestamp = Helper.dateTextToEpoch(startDateInput.getText().toString());
        endDateInTimestamp = Helper.dateTextToEpoch(endDateInput.getText().toString());

        if (!newCourse) {
            course.name = courseNameInput.getText().toString();
            course.startDate = startDateInTimestamp;
            course.endDate = endDateInTimestamp;
            course.instructorName = instructorNameInput.getText().toString();
            course.instructorEmail = instructorEmailInput.getText().toString();
            course.instructorPhone = instructorPhoneInput.getText().toString();
            course.courseStatus = courseStatusInput.getText().toString();
            courseDAO.updateCourse(course);
        } else {
            courseDAO.insertCourse(new Course(
                    courseNameInput.getText().toString(),
                    startDateInTimestamp,
                    endDateInTimestamp,
                    db.activeTerm,
                    instructorNameInput.getText().toString(),
                    instructorEmailInput.getText().toString(),
                    instructorPhoneInput.getText().toString(),
                    courseStatusInput.getText().toString()
                    ));
        }

        // now that we are finished with the course we can set it as null so that it is not loaded
        // into the add/edit course form when we want to add a new course
        db.activeCourse = null;
        Intent intent = new Intent(this, TermInfoCourseViewActivity.class);
        startActivity(intent);
    }
}
