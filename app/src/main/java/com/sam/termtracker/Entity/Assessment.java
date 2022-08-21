package com.sam.termtracker.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessments")
public class Assessment {

    Assessment(String name, int courseId) {
        this.name = name;
        this.courseId = courseId;
    }

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "name")
    String name;

    @ColumnInfo(name = "courseId")
    int courseId;
}
