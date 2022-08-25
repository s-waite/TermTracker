package com.sam.termtracker.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sam.termtracker.Entity.Course;
import com.sam.termtracker.Entity.Term;

import java.util.List;

@Dao
public interface CourseDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCourse(Course course);

    @Delete
    void deleteCourse(Course course);

    @Update
    void updateCourse(Course course);

    @Query("SELECT * from courses")
    List<Course> getAll();

    @Query("SELECT * FROM courses WHERE id = :id")
    Course getCourseById(int id);

    @Query("SELECT * FROM courses WHERE termId = :termId")
    List<Course> getAllCoursesWithTermId(int termId);
}
