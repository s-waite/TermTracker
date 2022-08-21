package com.sam.termtracker.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sam.termtracker.Entity.Assessment;
import com.sam.termtracker.Entity.Course;

import java.util.List;

@Dao
public interface AssessmentDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAssessment(Assessment assessment);

    @Delete
    void deleteAssessment(Assessment assessment);

    @Update
    void updateAssessment(Assessment assessment);

    @Query("SELECT * FROM assessments")
    List<Assessment> getAll();

    @Query("SELECT * FROM assessments WHERE id = :id")
    Assessment getAssessmentById(int id);
}
