package com.example.forcapp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button dictionaryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        setUI();

    }

    private void setUI() {
        dictionaryButton = findViewById(R.id.diccionario_bt);

        dictionaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dictionaryIntent = new Intent(getApplicationContext(), Dictionary.class);
                startActivity(dictionaryIntent);
            }
        });

    }
}