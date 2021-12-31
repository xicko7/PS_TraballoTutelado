package com.example.forcapp.database;

import android.content.Context;

import androidx.room.Room;

public class WordDatabaseClient {
    private Context mCtx;
    private static WordDatabaseClient mInstance;
    private WordDatabase_Impl wordDatabase; //our app database object

    private WordDatabaseClient(Context mCtx) {
        this.mCtx = mCtx;
        //creating the app database with Room database builder
        wordDatabase = (WordDatabase_Impl) Room.databaseBuilder(mCtx, WordDatabase.class, "Words.db").build();
// see also option .allowMainThreadQueries() (NOT recommended)
    }

    public static synchronized WordDatabaseClient getInstance(Context mCtx) {
        if (mInstance == null)
            mInstance = new WordDatabaseClient(mCtx);
        return mInstance;
    }

    public WordDatabase_Impl getWordDatabase() {
        return wordDatabase;
    }

}
