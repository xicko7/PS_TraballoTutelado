package com.example.forcapp;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forcapp.database.WordDatabase;
import com.example.forcapp.database.WordDatabaseClient;
import com.example.forcapp.entity.Word;

import java.util.ArrayList;
import java.util.List;


public class Dictionary extends AppCompatActivity {

    RecyclerView recyclerView;
    Button add, back;
    private WordsAdapter mAdapter;
    ArrayList<Word> initialData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary_layout);
        getSupportActionBar().hide();

        // Engadir as palabras por defecto da app
        setUI();

        // Engadir palabras da base de datos
        showCurrentWords(mAdapter);

    }

    private void setUI() {

        recyclerView = findViewById(R.id.word_rv);
        add = findViewById(R.id.button_addWord);
        back = findViewById(R.id.dictionary_back_button);

        mAdapter = new WordsAdapter(initialData);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mAdapter.setClickListener(new WordsAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                createDeleteDialog(mAdapter, position);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAddDialog(mAdapter);
            }
        });

    }

    private void createAddDialog(WordsAdapter mAdapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_dialog);

        EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton(R.string.dialogOK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (String.valueOf(input.getText()).length() > 1) {
                    String word = String.valueOf(input.getText()).substring(0, 1).toUpperCase() + String.valueOf(input.getText()).substring(1).toLowerCase();
                    insertWord(new Word(word), mAdapter);
                }else if (String.valueOf(input.getText()).length() == 1) {
                    String word = String.valueOf(input.getText()).toUpperCase();
                    insertWord(new Word(word), mAdapter);
                }
            }
        });
        builder.setNegativeButton(R.string.dialogCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Cancelar
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void createDeleteDialog(WordsAdapter mAdapter, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.remove_dialog);

        builder.setPositiveButton(R.string.dialogAcept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeWord(mAdapter, position);
            }
        });
        builder.setNegativeButton(R.string.dialogCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Cancelar
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void insertWord(Word word, WordsAdapter mAdapter) {
        //Controlar que no se repita la palabra
        class InsertWord extends AsyncTask<Void, Void, Word> { // claseinterna
            @Override
            protected Word doInBackground(Void... voids) {
                int finded = WordDatabaseClient.getInstance(getApplicationContext()).getWordDatabase().getWordDao().existsWord(word.getWord());
                if (finded == 0)
                    WordDatabaseClient.getInstance(getApplicationContext()).getWordDatabase().getWordDao().insertWord(word);
                return word;
            }

            @Override
            protected void onPostExecute(Word word) {
                super.onPostExecute(word);// Actualizar la UI
                mAdapter.addWord(word.getWord());
            }
        }
        InsertWord gf = new InsertWord(); // Crear una instancia y ejecutar
        gf.execute();
    }


    private void removeWord(WordsAdapter mAdapter, int pos) {
        class RemoveWord extends AsyncTask<Void, Void, Word> { // claseinterna
            @Override
            protected Word doInBackground(Void... voids) {
                // Controlar que non se repita a palabra (aforrar dependencias co recyclerView
                Word removed = mAdapter.getWord(pos);
                WordDatabaseClient.getInstance(getApplicationContext()).getWordDatabase().getWordDao().deleteWordByName(mAdapter.getWord(pos).getWord());
                return null;
            }

            @Override
            protected void onPostExecute(Word word) {
                super.onPostExecute(word); // Actualizar la UI
                mAdapter.removeWord(pos);
            }
        }
        RemoveWord gf = new RemoveWord(); // Crear una instancia y ejecutar
        gf.execute();

    }

    private void showCurrentWords(WordsAdapter mAdapter) {
        class GetAllWords extends AsyncTask<Void, Void, List<Word>> { // claseinterna
            @Override
            protected List<Word> doInBackground(Void... voids) {
                List<Word> words = WordDatabaseClient.getInstance(getApplicationContext()).getWordDatabase().getWordDao().getAllWords();
                return words;
            }

            @Override
            protected void onPostExecute(List<Word> words) {
                super.onPostExecute(words); // Actualizar la UI
                for (int i = 0; i < words.size(); i++)
                    mAdapter.addWord(words.get(i).getWord());
            }
        }
        GetAllWords gf = new GetAllWords(); // Crear una instancia y ejecutar
        gf.execute();
    }

}