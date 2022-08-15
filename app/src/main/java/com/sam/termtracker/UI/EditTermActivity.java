package com.sam.termtracker.UI;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
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

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class EditTermActivity extends AppCompatActivity {
    TextInputEditText termNameInput;
    TextInputEditText startDateInput;
    TextInputEditText endDateInput;
    TextInputLayout endDateInputLayout;
    Term term;
    Boolean newTerm;
    Boolean formHasError;
    Database db;
    TermDAO termDAO;
    MaterialDatePicker<Long> startDatePicker;
    MaterialDatePicker<Long> endDatePicker;
    int startDateInTimestamp;
    int endDateInTimestamp;
    ActionBar actionBar;
    AlertDialog errorWithInputsDialog;


    /**
     * Activity for creating and editing terms
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_term);

        // Get a reference to our DAO
        db = Database.getDatabase(getApplication());
        termDAO = db.termDao();

        actionBar = getSupportActionBar();

        initializeViews();

        // Check if any extras are passed. If they are, this activity is editing a term
        // If not then we are creating a new term
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            term = loadTermInfoIntoFields(extras);
            // this bool keeps track of whether we are editing or creating a new term
            newTerm = false;
            actionBar.setTitle("Edit Term");
        } else {
            newTerm = true;
            actionBar.setTitle("Add Term");
        }
    }

    /**
     * Called in onCreate, get references to the views of the layout and define the click listeners
     */
    private void initializeViews() {
        termNameInput = findViewById(R.id.termName);
        startDateInput = findViewById(R.id.startDate);
        endDateInput = findViewById(R.id.endDate);
        endDateInputLayout = findViewById(R.id.endDateLayout);

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
     * When editing a term, loads the info from the term into the edit form.
     * @param extras the data passed to the activity
     * @return the term object that was passed as an extra from another activity to this
     */
    private Term loadTermInfoIntoFields(Bundle extras) {
        DateTimeFormatter formatter = Helper.formatter;
        int termId = extras.getInt("termId");
        Term term = termDAO.getTermById(termId);

        LocalDateTime startDateTime = LocalDateTime.ofEpochSecond(term.startDate, 0, ZoneOffset.UTC);
        String startDate = formatter.format(startDateTime);
        LocalDateTime endDateTime = LocalDateTime.ofEpochSecond(term.endDate, 0, ZoneOffset.UTC);
        String endDate = formatter.format(endDateTime);

        String termName = term.termName;

        termNameInput.setText(termName);
        startDateInput.setText(startDate);
        endDateInput.setText(endDate);
        return term;
    }

    /**
     * onClick method for the save FAB.
     *
     * Creates a new term object of updates an existing one
     * @param view
     */
    public void saveTerm(View view) {
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