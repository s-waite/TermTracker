package com.sam.termtracker.Database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sam.termtracker.DAO.AssessmentDAO;
import com.sam.termtracker.DAO.CourseDAO;
import com.sam.termtracker.DAO.NoteDAO;
import com.sam.termtracker.Entity.Assessment;
import com.sam.termtracker.Entity.Note;
import com.sam.termtracker.Entity.Term;
import com.sam.termtracker.Entity.Course;
import com.sam.termtracker.DAO.TermDAO;

import java.util.List;

@androidx.room.Database(entities = {Term.class, Course.class, Assessment.class, Note.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
    public abstract TermDAO termDao();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();
    public abstract NoteDAO noteDAO();

    private static volatile Database INSTANCE;
    public Integer activeTerm;
    public Integer activeCourse;

    public static Database getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Database.class, "myScheduleDatabase.db")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

