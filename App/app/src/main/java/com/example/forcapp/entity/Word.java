package com.example.forcapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "words")
public class Word{

    @PrimaryKey(autoGenerate = true)
    Integer id;

    public void setWord(String word) {
        this.word = word;
    }

    @ColumnInfo(name = "word")
    String word;

    public Word(String word) {
        this.word = word;
    }

    public Integer getId() {
        return id;
    }

    public String getWord() {
        return word;
    }
}
