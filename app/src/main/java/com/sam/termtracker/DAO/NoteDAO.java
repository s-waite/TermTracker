package com.sam.termtracker.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sam.termtracker.Entity.Assessment;
import com.sam.termtracker.Entity.Note;

import java.util.List;

@Dao
public interface NoteDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Update
    void updateNote(Note note);

    @Query("SELECT * FROM notes ORDER BY title ASC")
    List<Note> getAll();

    @Query("SELECT * FROM notes WHERE courseId = :courseId")
    List<Note> getAllByCourseId(int courseId);

    @Query("SELECT * FROM notes WHERE id = :id")
    Note getNoteById(int id);
}
