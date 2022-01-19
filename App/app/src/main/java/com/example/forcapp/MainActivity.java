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

import com.example.forcapp.entity.Word;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<Word> defaultWordList;
    TextView title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        defaultWordList = createDefaultWordList();
        setUI();
    }

    public List<Word> createDefaultWordList() {

        List<Word> defaultWordList = new ArrayList<>();

        defaultWordList.add(new Word(getString(R.string.word1)));
        defaultWordList.add(new Word(getString(R.string.word2)));
        defaultWordList.add(new Word(getString(R.string.word3)));
        defaultWordList.add(new Word(getString(R.string.word4)));
        defaultWordList.add(new Word(getString(R.string.word5)));
        defaultWordList.add(new Word(getString(R.string.word6)));
        defaultWordList.add(new Word(getString(R.string.word7)));
        defaultWordList.add(new Word(getString(R.string.word8)));
        defaultWordList.add(new Word(getString(R.string.word9)));
        defaultWordList.add(new Word(getString(R.string.word10)));
        defaultWordList.add(new Word(getString(R.string.word11)));
        defaultWordList.add(new Word(getString(R.string.word12)));
        defaultWordList.add(new Word(getString(R.string.word13)));
        defaultWordList.add(new Word(getString(R.string.word14)));
        defaultWordList.add(new Word(getString(R.string.word15)));
        defaultWordList.add(new Word(getString(R.string.word16)));
        defaultWordList.add(new Word(getString(R.string.word17)));
        defaultWordList.add(new Word(getString(R.string.word18)));
        defaultWordList.add(new Word(getString(R.string.word19)));
        defaultWordList.add(new Word(getString(R.string.word20)));
        defaultWordList.add(new Word(getString(R.string.word21)));
        defaultWordList.add(new Word(getString(R.string.word22)));
        defaultWordList.add(new Word(getString(R.string.word23)));
        defaultWordList.add(new Word(getString(R.string.word24)));
        defaultWordList.add(new Word(getString(R.string.word25)));
        defaultWordList.add(new Word(getString(R.string.word26)));
        defaultWordList.add(new Word(getString(R.string.word27)));
        defaultWordList.add(new Word(getString(R.string.word28)));
        defaultWordList.add(new Word(getString(R.string.word29)));
        defaultWordList.add(new Word(getString(R.string.word30)));
        defaultWordList.add(new Word(getString(R.string.word31)));
        defaultWordList.add(new Word(getString(R.string.word32)));
        defaultWordList.add(new Word(getString(R.string.word33)));
        defaultWordList.add(new Word(getString(R.string.word34)));
        defaultWordList.add(new Word(getString(R.string.word35)));
        defaultWordList.add(new Word(getString(R.string.word36)));

        return defaultWordList;
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