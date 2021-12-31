package com.example.forcapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button singleplayerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        setUI();
    }
    private void setUI() {

        singleplayerButton = findViewById(R.id.singleplayer_bt);
        singleplayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singleplayerIntent = new Intent(getApplicationContext(), Singleplayer.class);
                startActivity(singleplayerIntent);
            }
        });

    }

}