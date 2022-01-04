package com.example.forcapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.forcapp.database.WordDatabaseClient;
import com.example.forcapp.entity.Word;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button dictionaryButton;
    Button singleplayerButton;
    List<Word> defaultWordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        setUI();
        insertDefaultWordList(createDefaultWordList());
    }

    public List<Word> createDefaultWordList() {
        if (!defaultWordList.isEmpty()) {
            defaultWordList.clear();
        }
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

        return defaultWordList;
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

        singleplayerButton = findViewById(R.id.singleplayer_bt);
        singleplayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singleplayerIntent = new Intent(getApplicationContext(), Singleplayer.class);
                startActivity(singleplayerIntent);
            }
        });

    }


    private void insertDefaultWordList(List<Word> wordList) {
        class InsertWordList extends AsyncTask<Void, Void, List<Word>> {
            @Override
            protected List<Word> doInBackground(Void... voids) {
                //Permite que cada vez que entremos no dicionario e esté baleiro meta as palabras novamente
                //Con isto conseguimos ter sempre palabras dentro da BBDD, que nos aforrará problemas no futuro
                if (WordDatabaseClient.getInstance(getApplicationContext()).getWordDatabase().getWordDao().getAllWords().isEmpty()) {
                    WordDatabaseClient.getInstance(getApplicationContext()).getWordDatabase().getWordDao().insertWordList(wordList);
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Word> words) {
                super.onPostExecute(words);
            }
        }
        InsertWordList gf = new InsertWordList(); // Crear una instancia y ejecutar
        gf.execute();
    }

}