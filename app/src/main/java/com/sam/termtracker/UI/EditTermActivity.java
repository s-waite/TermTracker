package com.sam.termtracker.UI;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sam.termtracker.DAO.TermDAO;
import com.sam.termtracker.Database.Database;
import com.sam.termtracker.Entity.Term;
import com.sam.termtracker.Helper;
import com.sam.termtracker.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class EditTermActivity extends AppCompatActivity {
    TextInputEditText termNameInput;
    TextInputEditText startDateInput;
    TextInputEditText endDateInput;

    TextInputLayout startDateInputLayout;
    TextInputLayout endDateInputLayout;

    Term term;
    Boolean newTerm;
    Boolean formHasError;
    Boolean nameHasError;
    Boolean otherDateFieldEmpty;
    Database db;
    TermDAO termDAO;
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
        setContentView(R.layout.activity_edit_term);

        // Get a reference to the DAO
        db = Database.getDatabase(getApplication());
        termDAO = db.termDao();

        actionBar = getSupportActionBar();
        initializeViews();

        // Create two date pickers that register confirmation listeners that change the supplied
        // text input to the result of the date picker
        startDatePicker = Helper.buildDatePicker(startDateInput);
        endDatePicker = Helper.buildDatePicker(endDateInput);


        // Check if active term is null
        // If it is then we are creating a new term
        if (db.activeTerm != null) {
            term = termDAO.getTermById(db.activeTerm);
            loadTermInfoIntoFields(term);
            // this bool keeps track of whether we are editing or creating a new term
            newTerm = false;
            actionBar.setTitle("Edit Term");
        } else {
            newTerm = true;
            actionBar.setTitle("Add Term");
        }
        initializeClickListeners();
    }

    /**
     * Called in onCreate, get references to the views of the layout and define the click listeners
     */
    private void initializeViews() {
        termNameInput = findViewById(R.id.termName);
        startDateInput = findViewById(R.id.startDate);
        endDateInput = findViewById(R.id.endDate);

        startDateInputLayout = findViewById(R.id.startDateLayout);
        endDateInputLayout = findViewById(R.id.endDateLayout);

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

    /**
     * When editing a term, loads the info from the term into the edit form.
     *
     * @param term the term to load data from
     */
    private void loadTermInfoIntoFields(Term term) {
        String startDate = Helper.epochToString(term.startDate);
        String endDate = Helper.epochToString(term.endDate);
        String termName = term.termName;

        termNameInput.setText(termName);
        startDateInput.setText(startDate);
        endDateInput.setText(endDate);
    }


    /**
     * onClick method for the save FAB.
     * <p>
     * Creates a new term object of updates an existing one
     *
     * @param view
     */
    public void saveTerm(View view) {
        // Check if term name is empty

        if (termNameInput.getText().toString().isEmpty()) {
            nameHasError = true;
        } else {
            nameHasError = false;
        }

        if (formHasError || nameHasError) {
            errorWithInputsDialog.show();
            return;
        }

        startDateInTimestamp = Helper.dateTextToEpoch(startDateInput.getText().toString());
        endDateInTimestamp = Helper.dateTextToEpoch(endDateInput.getText().toString());

        if (!newTerm) {
            term.termName = termNameInput.getText().toString();
            term.startDate = startDateInTimestamp;
            term.endDate = endDateInTimestamp;
            termDAO.updateTerm(term);
        } else {
            termDAO.insertTerm(new Term(termNameInput.getText().toString(), startDateInTimestamp, endDateInTimestamp));
        }
        Intent intent = new Intent(this, TermViewActivity.class);
        startActivity(intent);
    }
}