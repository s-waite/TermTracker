package com.sam.termtracker.Database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sam.termtracker.DAO.CourseDAO;
import com.sam.termtracker.Entity.Term;
import com.sam.termtracker.Entity.Course;
import com.sam.termtracker.DAO.TermDAO;

import java.util.List;

@androidx.room.Database(entities = {Term.class, Course.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
    public abstract TermDAO termDao();
    public abstract CourseDAO courseDAO();

    private static volatile Database INSTANCE;

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
//    public List<Term> getAllTerms() {
//        return termDao().getAll();
//    }
//
//    public void insertTerm(Term term) {
//        termDao().insert(term);
//    }
//
//    public void deleteTerm(Term term) {
//        termDao().delete(term);
//    }
//
//    public void updateTerm(Term term) {
//        termDao().updateTerm(term);
//    }
//
//    public Term getTermById(int id) {
//        return termDao().getTermById(id);
//    }
}

