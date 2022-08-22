package com.sam.termtracker.UI.recyclerAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sam.termtracker.DAO.TermDAO;
import com.sam.termtracker.Database.Database;
import com.sam.termtracker.Entity.Term;
import com.sam.termtracker.R;
import com.sam.termtracker.UI.form.EditTermActivity;
import com.sam.termtracker.UI.TermInfoCourseViewActivity;

import java.util.List;


/**
 * Recycler adapter for displaying a list of terms with options for each term
 */
public class TermRecyclerAdapter extends RecyclerView.Adapter<TermRecyclerAdapter.ViewHolder> {
    private List<Term> localDataSet;
    private AlertDialog deleteTermAlertDialog;
    private Context context;
    private TermDAO termDAO;
    private Database db;

    /**
     * A reference for the views within each recycler item
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView myTextView;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        public ViewHolder(View view) {
            super(view);
            myTextView = (TextView) view.findViewById(R.id.itemTextView);
            editButton = view.findViewById(R.id.editButton);
            deleteButton = view.findViewById(R.id.deleteButton);
            db = Database.getDatabase(context);
            termDAO = db.termDao();

            // Open the term info along with course list when this item is pressed
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TermInfoCourseViewActivity.class);
                    // Set the active term in the database
                    db.activeTerm = localDataSet.get(getAdapterPosition()).id;
                    context.startActivity(intent);
                }
            });
        }

        public TextView getTextView() {
            return myTextView;
        }
        public ImageButton getEditButton() {
            return editButton;
        }
        public ImageButton getDeleteButton() {
            return deleteButton;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public TermRecyclerAdapter(List<Term> dataSet, Context context) {
        localDataSet = dataSet;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_term, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        // click listener for "edit" button
        viewHolder.getEditButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditTermActivity.class);
                // Set the active term for the EditTermActivity to use
                db.activeTerm = localDataSet.get(position).id;
                context.startActivity(intent);
            }
        });

        // click listener for delete button
        viewHolder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    deleteTermAlertDialog = new MaterialAlertDialogBuilder(context)
                            .setTitle("Delete This Term?")
                            .setMessage("This cannot be undone")
                            .setPositiveButton("Confirm", (dialogInterface, i) -> {
                                termDAO.deleteTerm(localDataSet.get(position));
                                localDataSet.remove(position);
                                notifyItemRemoved(position);
                            })
                            .setNegativeButton("Cancel", ((dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            }))
                            .create();
                deleteTermAlertDialog.show();
            }
        });

        // Get element from the dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet.get(position).termName);
    }

    // Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

