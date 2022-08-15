package com.sam.termtracker.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sam.termtracker.Entity.Term;

import java.util.List;

@Dao
public interface TermDAO {
    @Query("SELECT * FROM term")
    List<Term> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTerm(Term term);

    @Delete
    void deleteTerm(Term term);

    @Query("SELECT * FROM term WHERE termId = :id")
    Term getTermById(int id);

    @Update
    void updateTerm(Term term);
}

