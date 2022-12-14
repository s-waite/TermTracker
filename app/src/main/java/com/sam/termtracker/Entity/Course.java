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

    public Course(String name,
                  int startDate,
                  int endDate,
                  int termId,
                  String instructorName,
                  String instructorEmail,
                  String instructorPhone,
                  String courseStatus,
                  Boolean notifyBeforeStart,
                  Boolean notifyBeforeEnd
    ) {
       this.name = name;
       this.startDate = startDate;
       this.endDate = endDate;
       this.termId = termId;
       this.instructorName = instructorName;
       this.instructorEmail = instructorEmail;
       this.instructorPhone = instructorPhone;
       this.courseStatus = courseStatus;
       this.notifyBeforeStart = notifyBeforeStart;
       this.notifyBeforeEnd = notifyBeforeEnd;
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

    @ColumnInfo(name = "courseStatus")
    public String courseStatus;

    @ColumnInfo(name = "notifyBeforeStart")
    public Boolean notifyBeforeStart;

    @ColumnInfo(name = "notifyBeforeEnd")
    public Boolean notifyBeforeEnd;
}
