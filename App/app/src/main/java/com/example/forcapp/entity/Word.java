package com.example.forcapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "words")
public class Word{

    @PrimaryKey(autoGenerate = true)
    Long id;

    @ColumnInfo(name = "word")
    String word;

    public Word(String word) {
        this.word = word;
    }

    public Long getId() {
        return id;
    }

    public String getWord() {
        return word;
    }
}
