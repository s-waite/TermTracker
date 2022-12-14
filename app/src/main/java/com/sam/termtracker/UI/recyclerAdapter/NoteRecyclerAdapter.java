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
import com.sam.termtracker.DAO.NoteDAO;
import com.sam.termtracker.Database.Database;
import com.sam.termtracker.Entity.Note;
import com.sam.termtracker.R;
import com.sam.termtracker.UI.form.EditCourseActivity;
import com.sam.termtracker.UI.form.EditNoteActivity;

import java.util.List;


public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder> {
    private List<Note> localDataSet;
    private Context context;
    private NoteDAO noteDAO;
    private Database db;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView contentView;
        private final ImageButton editButton;
        private final ImageButton deleteButton;
        private final ImageButton shareButton;

        public ViewHolder(View view) {
            super(view);
            titleView = (TextView) view.findViewById(R.id.itemTextView);
            contentView = view.findViewById(R.id.noteContent);
            editButton = view.findViewById(R.id.editButton);
            deleteButton = view.findViewById(R.id.deleteButton);
            shareButton = view.findViewById(R.id.shareButton);
            db = Database.getDatabase(context);
            noteDAO = db.noteDAO();
        }

        public TextView getTitleView() {
            return titleView;
        }

        public TextView getContentView() {
           return contentView;
        }

        public ImageButton getEditButton() {
            return editButton;
        }

        public ImageButton getDeleteButton() {
            return deleteButton;
        }

        public ImageButton getShareButton() { return shareButton; }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public NoteRecyclerAdapter(List<Note> dataSet, Context context) {
        localDataSet = dataSet;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_note, viewGroup, false);
        return new ViewHolder(view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        // click listener for "edit" button
        viewHolder.getEditButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditNoteActivity.class);
                intent.putExtra("id", localDataSet.get(viewHolder.getAdapterPosition()).id);
                context.startActivity(intent);
            }
        });

        viewHolder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, localDataSet.get(viewHolder.getAdapterPosition()).content);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                context.startActivity(shareIntent);
            }
        });

        viewHolder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog myDialog = new MaterialAlertDialogBuilder(context)
                        .setTitle("Delete This Note?")
                        .setMessage("This cannot be undone")
                        .setPositiveButton("Confirm", (dialogInterface, i) -> {
                            int currentPos = viewHolder.getAdapterPosition();
                            noteDAO.deleteNote(localDataSet.get(currentPos));
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
        viewHolder.getTitleView().setText(localDataSet.get(position).title);
        viewHolder.getContentView().setText(localDataSet.get(position).content);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

