package com.sam.termtracker.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "assessments")
public class Assessment {

    public Assessment(String name,
                      int courseId,
                      int startDate,
                      int endDate,
                      Boolean notifyBeforeStart,
                      Boolean notifyBeforeEnd,
                      String assessmentType) {
        this.name = name;
        this.courseId = courseId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notifyBeforeStart = notifyBeforeStart;
        this.notifyBeforeEnd = notifyBeforeEnd;
        this.assessmentType = assessmentType;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "courseId")
    public int courseId;

    @ColumnInfo(name = "startDate")
    public int startDate;

    @ColumnInfo(name = "endDate")
    public int endDate;

    @ColumnInfo(name = "notifyBeforeStart")
    public Boolean notifyBeforeStart;

    @ColumnInfo(name = "notifyBeforeEnd")
    public Boolean notifyBeforeEnd;

    @ColumnInfo(name = "assessmentType")
    public String assessmentType;
}
