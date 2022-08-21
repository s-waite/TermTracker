package com.sam.termtracker.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sam.termtracker.DAO.TermDAO;
import com.sam.termtracker.Database.Database;
import com.sam.termtracker.Entity.Term;
import com.sam.termtracker.R;
import com.sam.termtracker.UI.recyclerAdapter.TermRecyclerAdapter;

import java.util.List;

public class TermViewActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton fab;
    public AlertDialog myDialog;
    TermDAO termDAO;

    /**
     * Shows a list of terms and a button to create new ones.
     *
     * Uses the {@link TermRecyclerAdapter} to display the list of terms and control the click events.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_view);
        getSupportActionBar().setTitle("Terms");

        Database db = Database.getDatabase(getApplication());
        // Set the active term stored in the database to null because when we are in the term view
        // there is not 'active' term
        db.activeTerm = null;

        termDAO = db.termDao();
        List<Term> termList = termDAO.getAll();

        // Set up the recycler
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(new TermRecyclerAdapter(termList, this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Add FAB to create new term
        fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditTermActivity.class);
                startActivity(intent);
            }
        });


    }
}