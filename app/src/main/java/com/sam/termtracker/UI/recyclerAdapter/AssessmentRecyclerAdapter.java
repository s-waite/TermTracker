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
import com.sam.termtracker.DAO.AssessmentDAO;
import com.sam.termtracker.Database.Database;
import com.sam.termtracker.Entity.Assessment;
import com.sam.termtracker.R;
import com.sam.termtracker.UI.form.EditAssessmentActivity;
import com.sam.termtracker.UI.form.EditCourseActivity;

import java.util.List;


public class AssessmentRecyclerAdapter extends RecyclerView.Adapter<AssessmentRecyclerAdapter.ViewHolder> {
    private List<Assessment> localDataSet;
    private Context context;
    private AssessmentDAO assessmentDAO;
    private Database db;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.itemTextView);
            editButton = view.findViewById(R.id.editButton);
            deleteButton = view.findViewById(R.id.deleteButton);
            db = Database.getDatabase(context);
            assessmentDAO = db.assessmentDAO();
        }

        public TextView getTextView() {
            return textView;
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
    public AssessmentRecyclerAdapter(List<Assessment> dataSet, Context context) {
        localDataSet = dataSet;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_assessment, viewGroup, false);


        return new ViewHolder(view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        // click listener for "edit" button
        viewHolder.getEditButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditAssessmentActivity.class);
                intent.putExtra("id", localDataSet.get(position).id);
                context.startActivity(intent);
            }
        });

        viewHolder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog myDialog = new MaterialAlertDialogBuilder(context)
                        .setTitle("Delete This Course?")
                        .setMessage("This cannot be undone")
                        .setPositiveButton("Confirm", (dialogInterface, i) -> {
                            int currentPos = viewHolder.getAdapterPosition();
                            assessmentDAO.deleteAssessment(localDataSet.get(currentPos));
                            localDataSet.remove(currentPos);
                            notifyItemRemoved(currentPos);
                        })
                        .setNegativeButton("Cancel", ((dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        }))
                        .create();
                myDialog.show();

            }
        });

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet.get(position).name);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

