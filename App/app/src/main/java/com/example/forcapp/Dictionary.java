package com.example.forcapp;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forcapp.database.WordDatabaseClient;
import com.example.forcapp.entity.Word;

import java.util.ArrayList;
import java.util.List;


public class Dictionary extends AppCompatActivity {

    List<Word> defaultWordList = new ArrayList<>();
    RecyclerView recyclerView;
    private WordsAdapter mAdapter;
    ArrayList<Word> initialData = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.añadir_menu:
                createAddDialog(mAdapter);
                return true;
            case R.id.reset_menu:
                createResetBdDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary_layout);
        getSupportActionBar().setTitle(R.string.diccionario_bt);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Engadir as palabras por defecto da app
        setUI();
        insertDefaultWordList(createDefaultWordList(), 0);


    }

    private void setUI() {

        recyclerView = findViewById(R.id.word_rv);


        mAdapter = new WordsAdapter(initialData);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);


        mAdapter.setClickListener(new WordsAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                createDeleteDialog(mAdapter, position);
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
                String avaliableLetters = "qwertyuiopasdfghjklñzxcvbnmQWERTYUIOPASDFGHJKLÑZXCVBNMáÁéÉíÍóÓúÚ";
                String newWord = String.valueOf(input.getText());
                boolean avaliable = true;
                // Comprobar que non hai espazos
                for(int j = 0; j < newWord.length(); j++){
                    if(!avaliableLetters.contains(String.valueOf(newWord.charAt(j)))) {
                        avaliable = false;
                        Toast.makeText(getApplicationContext(), "Soamente se permiten letras. [A,Z]", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                if (avaliable){
                    String word = newWord.toUpperCase();
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

    private void createResetBdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.reset_dialog);

        builder.setPositiveButton(R.string.dialogAcept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                insertDefaultWordList(defaultWordList, 1);
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

    private List<Word> createDefaultWordList() {
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
                showCurrentWords(mAdapter);
            }
        }
        InsertWord gf = new InsertWord(); // Crear una instancia y ejecutar
        gf.execute();
    }

    private void insertDefaultWordList(List<Word> wordList, int reset) {
        class InsertWordList extends AsyncTask<Void, Void, List<Word>> {
            @Override
            protected List<Word> doInBackground(Void... voids) {
                if (reset == 1) {
                    WordDatabaseClient.getInstance(getApplicationContext()).getWordDatabase().getWordDao().deleteAllWords();
                }

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
                showCurrentWords(mAdapter);
            }
        }
        InsertWordList gf = new InsertWordList(); // Crear una instancia y ejecutar
        gf.execute();
    }

    private void removeWord(WordsAdapter mAdapter, int pos) {
        class RemoveWord extends AsyncTask<Void, Void, Word> { // claseinterna
            @Override
            protected Word doInBackground(Void... voids) {
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
                mAdapter.removeWords();
                for (int i = 0; i < words.size(); i++)
                    mAdapter.addWord(words.get(i).getWord().toUpperCase());
            }
        }
        GetAllWords gf = new GetAllWords(); // Crear una instancia y ejecutar
        gf.execute();
    }

}