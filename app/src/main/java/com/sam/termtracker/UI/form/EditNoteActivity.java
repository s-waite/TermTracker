package com.sam.termtracker.UI.form;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sam.termtracker.DAO.AssessmentDAO;
import com.sam.termtracker.DAO.NoteDAO;
import com.sam.termtracker.Database.Database;
import com.sam.termtracker.Entity.Assessment;
import com.sam.termtracker.Entity.Note;
import com.sam.termtracker.R;
import com.sam.termtracker.UI.CourseInfoAssessmentViewActivity;


public class EditNoteActivity extends AppCompatActivity {
    TextInputEditText titleInput;
    TextInputEditText contentInput;
    TextInputLayout titleInputLayout;
    TextInputLayout contentInputLayout;
    FloatingActionButton fab;
    AlertDialog errorDialog;
    Boolean newNote;

    Database db;
    int activeCourse;
    int noteId;
    Note note;
    NoteDAO noteDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        initializeViews();

        Database db = Database.getDatabase(getApplication());
        activeCourse = db.activeCourse;
        noteDAO = db.noteDAO();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            noteId = extras.getInt("id");
            note = noteDAO.getNoteById(noteId);
            loadDataIntoViews(note);
            newNote = false;
        } else {
            newNote = true;
        }
    }

    private void initializeViews() {
        titleInput = findViewById(R.id.title);
        titleInputLayout = findViewById(R.id.titleLayout);
        contentInput = findViewById(R.id.content);
        contentInputLayout = findViewById(R.id.contentLayout);
        fab = findViewById(R.id.floating_action_button);
        errorDialog = new MaterialAlertDialogBuilder(this)
                .setTitle("Error")
                .setMessage("Please make sure all forms are filled out correctly")
                .setPositiveButton("Dismiss", ((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }))
                .create();
    }

    private void loadDataIntoViews(Note note) {
        titleInput.setText(note.title);
        titleInput.setText(note.content);
    }

    public void saveAssessment(View view) {
        noteDAO.insertNote(new Note(titleInput.getText().toString(), contentInput.getText().toString(), activeCourse));
        Intent intent = new Intent(getApplicationContext(), CourseInfoAssessmentViewActivity.class);
        startActivity(intent);
    }
}