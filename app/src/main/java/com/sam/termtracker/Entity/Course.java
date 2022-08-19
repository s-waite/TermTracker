package com.sam.termtracker.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "courses",
        foreignKeys = @ForeignKey(entity = Term.class,
                parentColumns = "id",
                childColumns = "termId",
                onDelete = CASCADE))

public class Course {
    public Course() {
    }

    public Course(String name, int startDate, int endDate, int termId) {
       this.name = name;
       this.startDate = startDate;
       this.endDate = endDate;
       this.termId = termId;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "courseName")
    public String name;

    @ColumnInfo(name = "startDate")
    public int startDate;

    @ColumnInfo(name = "endDate")
    public int endDate;

    @ColumnInfo(name = "termId")
    public int termId;

    @ColumnInfo(name = "instructorName")
    public String instructorName;

    @ColumnInfo(name = "instructorPhone")
    public String instructorPhone;

    @ColumnInfo(name = "instructorEmail")
    public String instructorEmail;
}
