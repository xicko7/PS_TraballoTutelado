package com.example.forcapp;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.forcapp.database.WordDatabaseClient;
import com.example.forcapp.entity.Word;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getApplication().setTheme(R.style.Base_Theme_AppCompat_Light);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setUI();
    }

    private void setUI() {

        Button dictionaryButton = findViewById(R.id.diccionario_bt);
        dictionaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dictionaryIntent = new Intent(getApplicationContext(), Dictionary.class);
                startActivity(dictionaryIntent);
            }
        });

        Button singleplayerButton = findViewById(R.id.singleplayer_bt);
        singleplayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singleplayerIntent = new Intent(getApplicationContext(), Singleplayer.class);
                startActivity(singleplayerIntent);
            }
        });

        Button multiplayerButton = findViewById(R.id.multiplayer_bt);
        multiplayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent multiplayerIntent = new Intent(getApplicationContext(), LobbyActivity.class);
                startActivity(multiplayerIntent);
            }
        });

        title_tv = findViewById(R.id.title_tv);
        title_tv.setTextColor(getResources().getColor(R.color.iconColor1));

        Shader textShader = new LinearGradient(0, 0, title_tv.getPaint().measureText(title_tv.getText().toString()), title_tv.getTextSize(),
                new int[]{getResources().getColor(R.color.iconColor1),getResources().getColor(R.color.iconColor2) ,getResources().getColor(R.color.iconColor3)},
                new float[]{0, 1,2}, Shader.TileMode.CLAMP);

        title_tv.getPaint().setShader(textShader);


    }

}