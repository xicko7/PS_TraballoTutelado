package com.example.forcapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class PreGameActivity extends AppCompatActivity {
    private TextView ed_emailPlayer1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_game);

        setUI();
    }

    private void setUI(){
        ed_emailPlayer1 = findViewById(R.id.emailPlayer1_tv);
        ed_emailPlayer1.setText("mario.paez@udc.es");
    }
}