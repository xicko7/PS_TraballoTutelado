package com.example.forcapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.forcapp.entity.Word;

import java.util.List;

@Dao
public interface WordDao {

    //Insert word into Local database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWord(Word... words);

    //Delete word from local database
    @Delete
    void deleteWord(Word... words);

    //Query
    @Query("SELECT word FROM words WHERE id LIKE :id")
    Word getWord(Long id);

    @Query("SELECT word FROM words")
    List<Word> getAllWords();

}
