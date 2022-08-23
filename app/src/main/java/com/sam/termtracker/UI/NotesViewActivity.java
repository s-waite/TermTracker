package com.sam.termtracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sam.termtracker.DAO.NoteDAO;
import com.sam.termtracker.Database.Database;
import com.sam.termtracker.Entity.Note;
import com.sam.termtracker.R;
import com.sam.termtracker.UI.form.EditNoteActivity;
import com.sam.termtracker.UI.form.EditTermActivity;
import com.sam.termtracker.UI.recyclerAdapter.NoteRecyclerAdapter;

import java.util.List;

public class NotesViewActivity extends AppCompatActivity {
    NoteDAO noteDAO;
    RecyclerView recyclerView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_view);

        getSupportActionBar().setTitle("Notes");

        Database db = Database.getDatabase(getApplication());

        noteDAO = db.noteDAO();
        List<Note> noteList = noteDAO.getAll();

        // Set up the recycler
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(new NoteRecyclerAdapter(noteList, this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Add FAB to create new term
        fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditNoteActivity.class);
                startActivity(intent);
            }
        });


    }
}