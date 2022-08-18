package com.sam.termtracker.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.sam.termtracker.DAO.CourseDAO;
import com.sam.termtracker.Database.Database;
import com.sam.termtracker.Entity.Course;
import com.sam.termtracker.R;

import java.util.List;


public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder> {
    private List<Course> localDataSet;
    private Context context;
    private CourseDAO courseDAO;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
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
            courseDAO = Database.getDatabase(context).courseDAO();

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CourseInfoAssessmentViewActivity.class);
                    context.startActivity(intent);

                }
            });
        }

        public TextView getMyTextView() {
            return myTextView;
        }

        public ImageButton getMyImageButton() {
            return editButton;
        }

        public ImageButton getMyDeleteButton() {
            return deleteButton;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public CourseRecyclerAdapter(List<Course> dataSet, Context context) {
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
        viewHolder.getMyImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditCourseActivity.class);
                // pass the term object as an extra
                intent.putExtra("courseId", localDataSet.get(position).id);
                intent.putExtra("termId", localDataSet.get(position).termId);
                context.startActivity(intent);


            }
        });

        viewHolder.getMyDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog myDialog = new MaterialAlertDialogBuilder(context)
                        .setTitle("Delete This Course?")
                        .setMessage("This cannot be undone")
                        .setPositiveButton("Confirm", (dialogInterface, i) -> {
                            courseDAO.deleteCourse(localDataSet.get(position));
                            localDataSet.remove(position);
                            notifyItemRemoved(position);
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
        viewHolder.getMyTextView().setText(localDataSet.get(position).name);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

