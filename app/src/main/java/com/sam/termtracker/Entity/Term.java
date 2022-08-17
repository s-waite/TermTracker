package com.sam.termtracker.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "terms")
public class Term {
    public Term() {
    }

    public Term(String name, int startDate, int endDate) {
        this.termName = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "Name")
    public String termName;

    @ColumnInfo(name = "Start Date")
    public int startDate;

    @ColumnInfo(name = "End Date")
    public int endDate;
}

