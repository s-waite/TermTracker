package com.sam.termtracker.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {

    public Note(String title, String content, int courseId) {
        this.title = title;
        this.content = content;
        this.courseId = courseId;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "courseId")
    public int courseId;
}
