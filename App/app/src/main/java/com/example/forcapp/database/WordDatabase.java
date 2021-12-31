package com.example.forcapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.forcapp.dao.WordDao;
import com.example.forcapp.entity.Word;

@Database(version = 1, entities = {Word.class})
abstract class WordDatabase extends RoomDatabase {

    public abstract WordDao getWordDao();
}
