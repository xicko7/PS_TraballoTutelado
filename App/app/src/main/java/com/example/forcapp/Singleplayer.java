package com.example.forcapp;

import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

public class Singleplayer extends AppCompatActivity {

    private LetterAdapter adapter;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gridView = findViewById(R.id.letters);

        adapter = new LetterAdapter(getApplicationContext());
        gridView.setAdapter(adapter);
    }
}